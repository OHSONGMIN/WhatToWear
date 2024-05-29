package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "outfits")
public class Outfit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private int memberId;

    @Column
    private LocalDateTime regdate;

    @Column(length = 100, nullable = false)
    private String review;

    @Column(length = 20)
    private String region;

    @Column
    private int temperature;

    @Column
    private int delStatus;

    @Column(length = 100)
    private String overcoat;

    @Column(length = 100)
    private String top;

    @Column(length = 100)
    private String bottom;

    @Column(length = 100)
    private String accessory;
}
