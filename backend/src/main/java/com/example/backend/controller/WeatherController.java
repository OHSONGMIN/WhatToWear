package com.example.backend.controller;


import com.example.backend.dto.WeatherDto;
import com.example.backend.service.GridService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class WeatherController {

    private String apiUrl = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst";
    private String apiKey = "B7yvNvRj2awYzuewTUeXeYA3QUT5q19gCrX3sIlZrKQtv%2Bc7HhloJDeHQnlWN8%2BeA7VPqaHYiQ7ZlrmuzZ0OEQ%3D%3D"; //encoding

    @Autowired
    private GridService gridService;

    @Autowired
    private CacheManager cacheManager;

    @GetMapping("/api/main/weather")
    public ResponseEntity<?> getWeather(
            @RequestParam double lat, @RequestParam double lon) {

        //위도, 경도 -> 격자(x, y)
        Map<String, Double> gridXY = gridService.dfs_xy_conv("toXY", lat, lon);
        Double x = gridXY.get("x");
        Double y = gridXY.get("y");

        LocalDate now = LocalDate.now();

        String nowDate = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String nowTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH00"));

        //baseDate는 하루 이전
        String baseDate = now.minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        //baseTime은 2300으로 고정
        String baseTime = "2300";

        //캐시 데이더
        String cacheKey = baseDate + baseTime + "-" + lat + "-" + lon;
        String cachedData = getCachedWeatherData(cacheKey);

        if (cachedData != null) {
            List<WeatherDto.Response.Body.Items.WeaterItem> filteredData = filterWeatherData(cachedData, nowDate, nowTime);
            System.out.println("날씨 캐시 데이터");

            return new ResponseEntity<>(Map.of("response", Map.of("body", Map.of("items", Map.of("item", filteredData)))), HttpStatus.OK);
        }

        //API 요청
        String dataType = "JSON";

        String encodedApiKey = URLEncoder.encode(apiKey, StandardCharsets.UTF_8);
        String url = String.format("%s?serviceKey=%s&numOfRows=290&pageNo=1&base_date=%s&base_time=%s&nx=%.0f&ny=%.0f&dataType=%s", apiUrl, encodedApiKey, baseDate, baseTime, x, y, dataType);

        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        // RestTemplate을 사용해 API 요청 수행
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {

            String responseBody = response.getBody();

            if (responseBody != null && !responseBody.contains("SERVICE_KEY_IS_NOT_REGISTERED_ERROR")) {
                cacheWeatherData(cacheKey, responseBody);
                List<WeatherDto.Response.Body.Items.WeaterItem> filteredData = filterWeatherData(responseBody, nowDate, nowTime);

                return new ResponseEntity<>(Map.of("response", Map.of("body", Map.of("items", Map.of("item", filteredData)))), HttpStatus.OK);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("등록되지 않은 서비스 키입니다.");
            }
        } else {
            return ResponseEntity.status(response.getStatusCode()).body("날씨 정보를 불러오지 못했습니다.");
        }
    }

    private List<WeatherDto.Response.Body.Items.WeaterItem> filterWeatherData(String responseBody, String nowDate, String nowTime) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            WeatherDto weatherApiResponse = objectMapper.readValue(responseBody, WeatherDto.class);
            return weatherApiResponse.getResponse().getBody().getItems().getWeatherItem().stream()
                    .filter(item -> (nowDate.equals(item.getFcstDate()) && nowTime.equals(item.getFcstTime()))
                            || ("TMN".equals(item.getCategory())) || ("TMX".equals(item.getCategory())))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void cacheWeatherData(String cacheKey, String body) {
        cacheManager.getCache("weatherCache").put(cacheKey, body);
    }

    private String getCachedWeatherData(String cacheKey) {
        return cacheManager.getCache("weatherCache").get(cacheKey, String.class);
    }
}