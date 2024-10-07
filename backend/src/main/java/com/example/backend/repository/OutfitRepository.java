package com.example.backend.repository;

import com.example.backend.entity.Outfit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface OutfitRepository extends JpaRepository<Outfit, Integer> {

    @Query("SELECT o FROM Outfit o WHERE o.delStatus = 0")
    Page<Outfit> findOutfits(Pageable pageable);

    @Query("SELECT o FROM Outfit o WHERE o.delStatus = 0 AND o.memberId = :memberId ORDER BY o.id DESC")
    List<Outfit> findByMemberIdOrderByIdDesc(int memberId);

    @Query("SELECT o FROM Outfit o WHERE o.memberId = :memberId ORDER BY o.id DESC")
    List<Outfit> findAllOutfitsByMemberId(int memberId);

    Outfit findById(int id);

    @Query("SELECT o FROM Outfit o WHERE o.regdate BETWEEN :startDate AND :endDate ORDER BY o.id DESC")
    List<Outfit> findOutfitsRange(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT o FROM Outfit o ORDER BY o.id DESC")
    List<Outfit> findAllOutfits();
}
