package com.example.backend.repository;

import com.example.backend.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Member, Integer> {

    Boolean existsByEmail(String email);
}
