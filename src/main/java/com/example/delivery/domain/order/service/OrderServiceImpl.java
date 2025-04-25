package com.example.delivery.domain.order.service;

import com.example.delivery.common.exception.base.CustomException;
import com.example.delivery.common.exception.enums.ErrorCode;
import com.example.delivery.domain.order.dto.requestDTO.OrderRequestDTO;
import com.example.delivery.domain.order.dto.responseDTO.OrderResponseDTO;
import com.example.delivery.domain.order.entity.Order;
import com.example.delivery.domain.order.entity.OrderMenu;
import com.example.delivery.domain.order.repository.OrderRepository;
import com.example.delivery.domain.store.entity.Store;
import com.example.delivery.domain.store.repository.StoreRepository;
import com.example.delivery.domain.user.entity.User;
import com.example.delivery.domain.user.repository.UserRepository;
import com.example.delivery.domain.menu.entity.Menu;
import com.example.delivery.domain.menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

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
    public OrderResponseDTO createOrder(Long userId, OrderRequestDTO requestDTO) {
//       유저,가게
        User foundUser = userRepository.findById(userId)
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

        // 최소 주문 금액 검증
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

    @Override
    public Slice<OrderResponseDTO> getStoreOrders(Long storeId, Pageable pageable) {
        return null;
    }
}
