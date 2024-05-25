package com.example.backend.service;

import io.jsonwebtoken.Claims;

public interface JwtService {
    String getToken(String key, Object value);

    Claims getClaims(String token);

    //인자로 받은 token이 문제 없는지
    boolean isValid(String token);

    //token정보에서 사용자 id 값을 가져오는
    int getId(String token);
}
