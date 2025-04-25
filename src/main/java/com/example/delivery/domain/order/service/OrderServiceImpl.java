package com.example.delivery.domain.order.service;

import com.example.delivery.common.exception.base.CustomException;
import com.example.delivery.common.exception.enums.ErrorCode;
import com.example.delivery.domain.order.dto.requestDTO.OrderRequestDTO;
import com.example.delivery.domain.order.dto.responseDTO.OrderResponseDTO;
import com.example.delivery.domain.order.entity.Order;
import com.example.delivery.domain.order.entity.OrderMenu;
import com.example.delivery.domain.order.entity.OrderStatus;
import com.example.delivery.domain.order.repository.OrderRepository;
import com.example.delivery.domain.store.entity.Store;
import com.example.delivery.domain.store.repository.StoreRepository;
import com.example.delivery.domain.user.dto.SessionUserDto;
import com.example.delivery.domain.user.entity.User;
import com.example.delivery.domain.user.repository.UserRepository;
import com.example.delivery.menu.entity.Menu;
import com.example.delivery.menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;

    @Transactional
    @Override
    public OrderResponseDTO createOrder(SessionUserDto sessionUserDto, OrderRequestDTO requestDTO) {
//       유저,가게
        User foundUser = userRepository.findById(sessionUserDto.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Store foundStore = storeRepository.findById(requestDTO.getStoreId())
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));

        //영업시간 검증
        //현재시간(서울) 가져오기
        LocalTime now = LocalTime.now(ZoneId.of("Asia/Seoul"));
        if(now.isBefore(foundStore.getOpenTime()) || now.isAfter(foundStore.getCloseTime())){
            throw new CustomException(ErrorCode.STORE_CLOSED);
        }

        //  메뉴 리스트 -> OrderMenu 엔티티 변환
        List<OrderMenu> menuList = requestDTO.getMenus().stream()
                .map(m -> {
                    Menu foundMenu = menuRepository.findByIdOrElseThrow(m.getMenuId());
                    return m.toEntity(foundMenu);
                })
                .collect(toList());

        // 최소 주문 금액 검증 -> 상태검증 엔티티안에서 ?
        BigDecimal minOrderPrice = BigDecimal.valueOf(foundStore.getMinOrderPrice());
        Order order = Order.create(foundUser, foundStore, menuList, requestDTO.getRequestMessage());
        if (order.getTotalPrice().compareTo(minOrderPrice)<0) {
            throw new CustomException(ErrorCode.ORDER_MINIMUM_PRICE);
        }
        //결제(나중에)

        //toEntity
        Order saved = orderRepository.save(order);

        //toDTO
        return OrderResponseDTO.toDTO(saved);
    }

    //주문 목록(가게사장) 조회 Slice
    @Override
    @Transactional(readOnly = true)
    public Slice<OrderResponseDTO> getStoreOrders(Long userId,Long storeId, Pageable pageable) {
        //Owner 가게인지 검증
        Store foundStore = storeRepository.findMyStoreOrElseThrow(storeId, userId);

        //owner 주문목록(slice) 조회
        Slice<Order> foundOrder = orderRepository.findByStoreId(foundStore.getId(), pageable);
//        List<OrderResponseDTO> list = foundOrder.map(OrderResponseDTO::toDTO).toList();
//        return new SliceImpl<>(list,pageable, foundOrder.hasNext()
//        );
        return foundOrder.map(OrderResponseDTO::toDTO);
    }

//    가게사장 주문상세보기
    @Override
    @Transactional
    public OrderResponseDTO getDetail(Long userId, Long storeId, Long orderId) {
        //가게 & Owner 검증
        Store foundStore = storeRepository.findMyStoreOrElseThrow(storeId, userId);

       //주문 검증
        Order foundOrder = orderRepository.findByIdAndStoreIdOrElseThrow(orderId, foundStore.getId());

        return OrderResponseDTO.toDTO(foundOrder);
    }
}
