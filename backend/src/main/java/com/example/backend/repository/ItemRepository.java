package com.example.backend.repository;

import com.example.backend.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {
    //Item class의 Id 값인 id의 데이터 타입이 Integer였기 때문에 Integer를 넣어야 함

    List<Item> findByIdIn(List<Integer> ids); //주어진 ID 목록(ids)에 해당하는 Item 객체들의 리스트를 반환
    //Find By Id In Ids - ids List의 id로 Item객체들을 리스트 형태로 반환
}
