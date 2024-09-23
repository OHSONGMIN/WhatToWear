package com.example.backend.repository;

import com.example.backend.entity.Member;
import com.example.backend.entity.Outfit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AdminRepository extends JpaRepository<Member, Integer> {

    @Query("SELECT m from Member m WHERE m.email LIKE %:keyword%")
    List<Member> findByEmail(String keyword);

}
