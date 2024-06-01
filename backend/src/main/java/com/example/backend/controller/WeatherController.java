package com.example.backend.controller;


import com.example.backend.service.GridService;
import com.example.backend.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@RestController
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    private String apiUrl = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst";

    private String apiKey = "uS8%2FQmUqlRiReLrEtryTPbHovewvMG0ZYrDSRXSi0%2Bc8%2FAqCti8akMo%2Fu6w4LjjbzgY7Nyp5MZ2EpS8uKNWsYA%3D%3D";

    @Autowired
    private GridService gridService;

    @GetMapping("/api/weather")
    public ResponseEntity convertToGrid(
            @RequestParam double lat, @RequestParam double lon) throws IOException {

        //위도, 경도 -> 격자(x, y)
        Map<String, Double> gridXY = gridService.dfs_xy_conv("toXY", lat, lon);
        Double x = gridXY.get("x");
        Double y = gridXY.get("y");
        /*
        String type = "json";
        String numOfRows = "40";
*/

        LocalDate now = LocalDate.now();
        LocalTime time = LocalTime.now();

        String baseDate = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String baseTime = timeChange(time.format(DateTimeFormatter.ofPattern("HH00")));

        String dataType = "JSON";
        /*
        StringBuilder urlBuilder = new StringBuilder(apiUrl);
        urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "=" + apiKey);
        urlBuilder.append("&" + URLEncoder.encode("nx", "UTF-8") + "=" + URLEncoder.encode(x, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("ny", "UTF-8") + "=" + URLEncoder.encode(y, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("base_date", "UTF-8") + "=" + URLEncoder.encode(baseDate, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("base_time", "UTF-8") + "=" + URLEncoder.encode(baseTime, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("_type", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode(numOfRows, "UTF-8"));

        GET방식으로 전송해서 파라미터 받아오기
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");

*/


        //API 요청
        String url = String.format("%s?serviceKey=%s&numOfRows=290&pageNo=1&base_date=%s&base_time=%s&nx=%.0f&ny=%.0f&dataType=%s",
                apiUrl, apiKey, baseDate, baseTime, x, y, dataType);

        //RestTemplate을 사용해 API 호출
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        else {
            return ResponseEntity.status(response.getStatusCode()).body("Failed to fetch weather data");
        }
    }

    public String timeChange(String time)
    {
        /**
         시간은 3시간 단위로 조회해야 한다. 안그러면 정보가 없다고 뜬다.
         0200, 0500, 0800 ~ 2300까지
         그래서 시간을 입력했을때 switch문으로 조회 가능한 시간대로 변경해주었다.
         **/
        switch(time) {

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

