package com.example.backend.repository;

import com.example.backend.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Integer> {
    //Item class의 Id 값인 id의 데이터 타입이 Integer였기 때문에 Integer를 넣어야 함

}
