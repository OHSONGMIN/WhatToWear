package com.example.backend.dto;

import lombok.Getter;

@Getter
public class MemberInfoDto {

    private Integer memberId;

    public MemberInfoDto(int memberId) {

        this.memberId = memberId;
    }
}
