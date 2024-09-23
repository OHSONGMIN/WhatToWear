package com.example.backend.dto;

import lombok.Getter;

@Getter
public class MemberInfoDto {

    private Integer memberId;

    public MemberInfoDto(int memberId) {

        this.memberId = memberId;
    }
    //private String role;


//    public MemberInfoDto(int memberId, String role) {
//
//        this.memberId = memberId;
//        this.role = role;
//    }
}
