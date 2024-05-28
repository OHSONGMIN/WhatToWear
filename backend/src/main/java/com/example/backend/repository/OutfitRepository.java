package com.example.backend.repository;

import com.example.backend.entity.Outfit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OutfitRepository extends JpaRepository<Outfit, Integer> {

    @Query("SELECT o FROM Outfit o ORDER BY o.id DESC")
    List<Outfit> findAllOrderByIdDesc();
}
