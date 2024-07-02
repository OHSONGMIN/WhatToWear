package com.example.backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class WeatherDto {

    @JsonProperty("response")
    private Response response;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter
    @Setter
    public static class Response {

        @JsonProperty("body")
        private Body body;

        @JsonIgnoreProperties(ignoreUnknown = true)
        @Getter
        @Setter
        public static class Body {

            @JsonProperty("items")
            private Items items;

            @JsonIgnoreProperties(ignoreUnknown = true)
            @Getter
            @Setter
            public static class Items {

                @JsonProperty("item")
                private List<WeaterItem> weatherItem;

                @JsonIgnoreProperties(ignoreUnknown = true)
                @Getter
                @Setter
                public static class WeaterItem {

                    @JsonProperty("fcstDate")
                    private String fcstDate;

                    @JsonProperty("fcstTime")
                    private String fcstTime;

                    @JsonProperty("category")
                    private String category;

                    @JsonProperty("fcstValue")
                    private String fcstValue;
                }
            }
        }
    }

}


