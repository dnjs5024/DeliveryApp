package com.example.delivery.domain.store.service;

import com.example.delivery.common.exception.CustomException;
import com.example.delivery.common.exception.NotFoundException;
import com.example.delivery.common.exception.enums.ErrorCode;
import com.example.delivery.domain.store.dto.StoreRequestDto;
import com.example.delivery.domain.store.dto.StoreResponseDto;
import com.example.delivery.domain.store.entity.Store;
import com.example.delivery.domain.store.entity.StoreStatus;
import com.example.delivery.domain.store.repository.StoreRepository;
import com.example.delivery.domain.user.entity.User;
import com.example.delivery.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.delivery.common.constants.BusinessRuleConstants.MAX_STORE_LIMIT;


/**
 * 가게 관련 서비스 구현 클래스
 * 가게 생성, 수정, 단건/다건 조회, 상태 변경 기능을 포함
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    /**
     * 가게를 생성합니다.
     * 사장님 권한을 가진 유저만 가능하며, 한 명당 최대 3개의 가게까지 생성 가능합니다.
     *
     * @param storeRequestDto 생성 요청 정보
     * @param ownerId 요청한 유저 ID
     * @return 생성된 가게의 응답 DTO
     * @throws CustomException 유저가 사장님이 아니거나 가게 수 제한 초과 시
     */
    @Transactional
    @Override
    public StoreResponseDto createStore(StoreRequestDto storeRequestDto, Long ownerId) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        // 사장님만 생성 가능
        if (owner.getRole() != User.Role.OWNER) {
            throw new CustomException(ErrorCode.ONLY_OWNER_CREATE_STORE);
        }
        // 가게 수 제한
        if (storeRepository.countMyOpenStores(owner.getId()) >= MAX_STORE_LIMIT) {
            throw new CustomException(ErrorCode.STORE_LIMIT);
        }
        Store store = new Store(
                storeRequestDto.name(),
                storeRequestDto.openTime(),
                storeRequestDto.closeTime(),
                storeRequestDto.minOrderPrice(),
                StoreStatus.OPEN,
                owner
        );
        Store savedStore = storeRepository.save(store);
        return StoreResponseDto.from(savedStore);
    }

    /**
     * 가게 정보를 수정
     * 사장님 본인만 수정할 수 있으며, 이름/운영시간/최소 주문금액 수정이 가능합니다.
     *
     * @param storeId 수정할 가게 ID
     * @param ownerId 요청한 유저 ID
     * @param storeRequestDto 수정할 정보
     * @return 수정된 가게 응답 DTO
     * @throws NotFoundException 가게를 찾을 수 없는 경우
     * @throws CustomException 권한이 없는 경우
     */
    @Transactional
    @Override
    public StoreResponseDto updateStore(Long storeId, Long ownerId, StoreRequestDto storeRequestDto) {
        Store store = storeRepository.findMyStoreOrElseThrow(storeId, ownerId);

        // 값 변경 (Setter 대신 명시적인 메서드 또는 직접 할당)
        store.update(storeRequestDto.name(), storeRequestDto.openTime(), storeRequestDto.closeTime(), storeRequestDto.minOrderPrice());

        return StoreResponseDto.from(store);
    }

    /**
     * 단건 가게 정보를 조회합니다. (메뉴 포함)
     * 폐업 상태(CLOSED)의 가게는 조회되지 않습니다.
     *
     * @param storeId 가게 ID
     * @return 가게 정보 응답 DTO
     * @throws CustomException 존재하지 않는 경우
     */
    @Override
    public StoreResponseDto getStore(Long storeId) {
        Store store = storeRepository.findWithMenuByIdAndStatus(storeId, StoreStatus.OPEN)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));
        return StoreResponseDto.from(store);
    }

    @Override
    public List<StoreResponseDto> getMyStores(Long ownerId) {
        User user = userRepository.findById(ownerId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        if (user.getRole() != User.Role.OWNER) {
            throw new CustomException(ErrorCode.ONLY_OWNER_MANAGE_STORE);
        }
        return storeRepository.findAllMyStores(user.getId()).stream()
                .map(StoreResponseDto::from)
                .collect(Collectors.toList());
    }


    /**
     * 페이징 기반으로 OPEN 상태 가게 목록을 조회
     * keyword 가 있으면 키워드가 포함된, 오픈된 상태 가게만 조회되고 없으면 오픈 상태인 가게만 조회된다.
     * @param pageable 페이지 정보
     * @return 가게 목록 페이지 응답 DTO
     */
    @Override
    public Page<StoreResponseDto> getStoresByPage(Pageable pageable, String keyword) {
        Page<Store> stores = (keyword != null && !keyword.isBlank()) ?
                storeRepository.findByNameContainingAndStatus(keyword, StoreStatus.OPEN, pageable) :
                storeRepository.findAllByStatus(StoreStatus.OPEN, pageable);

        return stores.map(StoreResponseDto::from);
    }
    /**
     * 커서 기반으로 OPEN 상태 가게 목록을 조회
     * 첫 페이지 요청 시 lastId는 null
     *
     * @param lastId 마지막으로 조회된 가게 ID (null 가능)
     * @param size 조회할 데이터 개수
     * @return 가게 목록 응답 DTO 리스트
     */
    @Override
    public List<StoreResponseDto> getStoresByCursor(Long lastId, int size, String keyword) {
        Pageable pageable = PageRequest.of(0, size);
        List<Store> stores;
        if (keyword != null && !keyword.isBlank()) { // 키워드가 있으면
            stores = (lastId == null)
                    // 첫 커서가 null = 첫 페이지 요청이라는 뜻
                    // Status 조건에 맞는 가게 중에서 최신 가게 순서로 리스트를 불러오는 거고
                    ? storeRepository.findTopByStatusOrderByIdDesc(StoreStatus.OPEN, pageable)
                    // 첫 커서가 null이아닌 = 다음 페이지 요청
                    // id < lastId 조건을 만족하는
                    // 즉, 이전 커서에 해당하는 가게 중 최신 순으로 조회
                    : storeRepository.findTopByStatusAndIdLessThanOrderByIdDesc(StoreStatus.OPEN, lastId, pageable);


        } else { // 키워드가 없는 경우
            stores = (lastId == null)
                    ? storeRepository.findTopByStatusOrderByIdDesc(StoreStatus.OPEN, pageable)
                    : storeRepository.findTopByStatusAndIdLessThanOrderByIdDesc(StoreStatus.OPEN, lastId, pageable);
        }
        return stores.stream().map(StoreResponseDto::from).collect(Collectors.toList());
    }
    /**
     * 가게의 상태(운영/폐업 등)를 변경
     * 사장님 본인만 수행
     *
     * @param storeId 가게 ID
     * @param ownerId 요청한 유저 ID
     * @param status 변경할 상태 값
     * @throws CustomException 권한이 없거나 가게가 존재하지 않는 경우
     */
    @Transactional
    @Override
    public void changeStoreStatus(Long storeId, Long ownerId, StoreStatus status) {
        Store store = storeRepository.findMyStoreOrElseThrow(storeId, ownerId);
        store.changeStatus(status);
    }
}
