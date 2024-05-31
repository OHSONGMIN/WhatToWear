package com.example.backend.controller;


import com.example.backend.service.GridService;
import com.example.backend.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    private String apiUrl = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst";

    private String apiKey = "uS8%2FQmUqlRiReLrEtryTPbHovewvMG0ZYrDSRXSi0%2Bc8%2FAqCti8akMo%2Fu6w4LjjbzgY7Nyp5MZ2EpS8uKNWsYA%3D%3D";

    @Autowired
    private GridService gridService;

    @GetMapping("/api/grid")
    public ResponseEntity convertToGrid(
            @RequestParam double lat, @RequestParam double lon) {

        return new ResponseEntity<>(gridService.dfs_xy_conv("toXY", lat, lon), HttpStatus.OK);
    }
}
