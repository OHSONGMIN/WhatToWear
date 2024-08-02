package com.example.backend.controller;

import com.example.backend.dto.MemberDto;
import com.example.backend.service.JoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
public class JoinController {

    private JoinService joinService;

    public JoinController(JoinService joinService) {

        this.joinService = joinService;
    }

    @PostMapping("/join")
    public String joinProcess(@RequestBody MemberDto memberDto) {
        //@RequestBody 사용 - POSTMAN에서 JSON타입으로 POST
        //@RequestBody 어노테이션을 사용하지 않으면, Spring MVC는 기본적으로 폼 데이터를 객체에 바인딩할 수 있다.
        //즉, POSTMAN에서 데이터를 form-data 형식으로 전송할 수 있다.

        System.out.println("email이 넘어왔니? " + memberDto.getEmail());

        joinService.joinProcess(memberDto);

        return "ok";
    }
}
