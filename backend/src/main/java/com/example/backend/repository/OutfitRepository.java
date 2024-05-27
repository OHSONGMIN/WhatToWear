package com.example.backend.repository;

import com.example.backend.entity.Outfit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutfitRepository extends JpaRepository<Outfit, Integer> {

}
