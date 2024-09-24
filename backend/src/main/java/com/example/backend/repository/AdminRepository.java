package com.example.backend.repository;

import com.example.backend.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AdminRepository extends JpaRepository<Member, Integer> {

    @Query("SELECT m from Member m WHERE m.email LIKE %:keyword%")
    List<Member> findByKeyword(@Param("keyword") String keyword);

    Member findByEmail(String email);
}
