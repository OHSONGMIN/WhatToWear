package com.example.backend.repository;

import com.example.backend.entity.Outfit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OutfitRepository extends JpaRepository<Outfit, Integer> {

    @Query("SELECT o FROM Outfit o WHERE o.delStatus = 0 ORDER BY o.id DESC")
    List<Outfit> findOutfits();

    List<Outfit> findByMemberIdOrderByIdDesc(int memberId);

//    @Query("SELECT o FROM Outfit o JOIN FETCH o.details d JOIN FETCH d.item")
//    List<Outfit> findOutfitsWithDetails();

    //void updateDelStatus(int outfitId, int i);
}
