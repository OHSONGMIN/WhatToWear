package com.example.backend.controller;


import com.example.backend.service.GridService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
public class WeatherController {

    private String apiUrl = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst";
    //private String apiKey = "B7yvNvRj2awYzuewTUeXeYA3QUT5q19gCrX3sIlZrKQtv%2Bc7HhloJDeHQnlWN8%2BeA7VPqaHYiQ7ZlrmuzZ0OEQ%3D%3D"; //encoding
    private String apiKey = "B7yvNvRj2awYzuewTUeXeYA3QUT5q19gCrX3sIlZrKQtv+c7HhloJDeHQnlWN8+eA7VPqaHYiQ7ZlrmuzZ0OEQ=="; //decoding

    @Autowired
    private GridService gridService;

    @GetMapping("/api/weather")
    public ResponseEntity getWeather(
            @RequestParam double lat, @RequestParam double lon) {

        //위도, 경도 -> 격자(x, y)
        Map<String, Double> gridXY = gridService.dfs_xy_conv("toXY", lat, lon);
        Double x = gridXY.get("x");
        Double y = gridXY.get("y");

        LocalDate now = LocalDate.now();
        LocalTime time = LocalTime.now();

        String baseDate = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String baseTime = timeChange(time.format(DateTimeFormatter.ofPattern("HH00")));

        String dataType = "JSON";
        String encodedApiKey = URLEncoder.encode(apiKey, StandardCharsets.UTF_8);
        String url = String.format("%s?serviceKey=%s&numOfRows=290&pageNo=1&base_date=%s&base_time=%s&nx=%.0f&ny=%.0f&dataType=%s", apiUrl, encodedApiKey, baseDate, baseTime, x, y, dataType);

        System.out.println("Request URL: " + url); // 로그에 요청 URL 출력

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        System.out.println("Response: " + response);
        System.out.println("Request URL: " + url);
        System.out.println("Response Status Code: " + response.getStatusCode());
        System.out.println("Response Headers: " + response.getHeaders());
        System.out.println("Response Body: " + response.getBody());

        if (response.getStatusCode() == HttpStatus.OK) {
            return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
        } else {
            return ResponseEntity.status(response.getStatusCode()).body("Failed to fetch weather data");
        }
/*
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        UriComponents uriBuilder = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("serviceKey", apiKey)
                .queryParam("numOfRows", 290)
                .queryParam("pageNo", 1)
                .queryParam("base_date", baseDate)
                .queryParam("base_time", baseTime)
                .queryParam("nx", x.intValue())
                .queryParam("ny", y.intValue())
                .queryParam("dataType", "JSON")
                .build();

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, new HttpEntity<String>(headers), String.class);
        System.out.println("Request URL: " + uriBuilder);
*/

        //API 요청
        //String url = String.format("%s?serviceKey=%s&numOfRows=290&pageNo=1&base_date=%s&base_time=%s&nx=%.0f&ny=%.0f&dataType=%s", apiUrl, apiKey, baseDate, baseTime, x, y, dataType);

        /*
        String url = String.format("%s?serviceKey=%s&numOfRows=290&pageNo=1&base_date=%s&base_time=%s&nx=%d&ny=%d",
                apiUrl, apiKey, baseDate, baseTime, x.intValue(), y.intValue());
*/

  /*      System.out.println("Request URL: " + url); // 로그에 요청 URL 출력


        //RestTemplate을 사용해 API 호출
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        System.out.println("Response: " + response);

        if (response.getStatusCode() == HttpStatus.OK) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return ResponseEntity.status(response.getStatusCode()).body("Failed to fetch weather data");
        }

   */
    }

    public String timeChange(String time) {
        /**
         시간은 3시간 단위로 조회해야 한다. 안그러면 정보가 없다고 뜬다.
         0200, 0500, 0800 ~ 2300까지
         그래서 시간을 입력했을때 switch문으로 조회 가능한 시간대로 변경해주었다.
         **/

        switch (time) {

            case "0200":
            case "0300":
            case "0400":
                time = "0200";
                break;
            case "0500":
            case "0600":
            case "0700":
                time = "0500";
                break;
            case "0800":
            case "0900":
            case "1000":
                time = "0800";
                break;
            case "1100":
            case "1200":
            case "1300":
                time = "1100";
                break;
            case "1400":
            case "1500":
            case "1600":
                time = "1400";
                break;
            case "1700":
            case "1800":
            case "1900":
                time = "1700";
                break;
            case "2000":
            case "2100":
            case "2200":
                time = "2000";
                break;
            default:
                time = "2300";

        }
        return time;
    }
}

