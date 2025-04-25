package com.example.delivery.domain.store.repository;

import com.example.delivery.domain.store.entity.Store;
import com.example.delivery.domain.store.entity.StoreStatus;
import com.example.delivery.domain.user.entity.User;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class StoreRepositoryTest {
    @Autowired private StoreRepository storeRepository;

    @Autowired private EntityManager em;

    @Test
    @DisplayName("사장님이 운영중인 가게 수 조회")
    void countByOwnerIdAndStatus( ) {
        //given
        User owner = createOwner("사장님");
        em.persist(owner);

        Store store1 = createStore("BBQ", owner);
        Store store2 = createStore("BHC", owner);
        storeRepository.save(store1);
        storeRepository.save(store2);

        //when
        int count = storeRepository.countByOwnerIdAndStatus(owner.getId(), StoreStatus.OPEN);
    }


    @Test
    @DisplayName("가게명을 기준으로 검색하고 폐업 가게는 제외한다.")
    void findByNameContainingAndStatus( ) {
        //given
        User owner = createOwner("사장");
        em.persist(owner);
        Store openStore = createStore("김밥천국", owner);
        Store closedStore = createStore("김밥나라", owner);
        closedStore.changeStatus(StoreStatus.CLOSED);

        storeRepository.saveAll(List.of(openStore, closedStore));



    }
    private User createOwner(String name) {
        return new User(name + "@test.com", "1234", User.Role.OWNER, name);
    }

    private Store createStore(String name, User owner) {
        return Store.builder()
                .name(name)
                .openTime(LocalTime.of(10, 0 ))
                .closeTime(LocalTime.of(22,0))
                .minOrderPrice(100000)
                .owner(owner)
                .build();
    }


}