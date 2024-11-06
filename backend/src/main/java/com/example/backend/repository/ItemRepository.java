package com.example.backend.repository;

import com.example.backend.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {

    @Query("SELECT i FROM Item i WHERE i.activeStatus = 1")
    List<Item> findActiveItem();

    //List<Item> findByCategory(int category);
}
