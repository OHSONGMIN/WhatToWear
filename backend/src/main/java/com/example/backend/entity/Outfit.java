package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

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

    @Column(nullable = false)
    private LocalDateTime regdate;

    @Column(length = 100, nullable = false)
    private String review;

    @Column
    private int overcoat;

    @Column
    private int top;

    @Column
    private int bottom;

    @Column
    private int accessory;

    @Column(length = 20)
    private String region;

    @Column
    private int temperature;

    @Column
    private int delStatus;
}
