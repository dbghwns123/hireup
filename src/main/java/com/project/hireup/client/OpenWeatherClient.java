package com.project.hireup.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.project.hireup.entity.Weather;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class OpenWeatherClient {

  @Value("${openweathermap.key}")
  private String apiKey;

  private final RestTemplate restTemplate = new RestTemplate();

  public Weather getCurrentWeather(String city) {
    String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey
        + "&units=metric&lang=kr";

    ResponseEntity<JsonNode> response = restTemplate.getForEntity(url, JsonNode.class);

    JsonNode body = response.getBody();
    if (body == null) throw new RuntimeException("날씨 데이터 응답 없음");

    return Weather.builder()
        .weather(body.get("weather").get(0).get("main").asText())
        .description(body.get("weather").get(0).get("description").asText())
        .icon("http://openweathermap.org/img/w/" + body.get("weather").get(0).get("icon").asText() + ".png")
        .temperature(body.get("main").get("temp").asDouble())
        .build();
  }
}

