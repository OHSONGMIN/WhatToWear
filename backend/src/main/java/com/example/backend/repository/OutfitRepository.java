package com.example.backend.repository;

import com.example.backend.entity.Outfit;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OutfitRepository extends JpaRepository<Outfit, Integer> {

    @Query("SELECT o FROM Outfit o WHERE o.delStatus = 0 ORDER BY o.id DESC")
    List<Outfit> findOutfits();

    //void updateDelStatus(int outfitId, int i);
}
