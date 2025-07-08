package com.project.hireup.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Weather {

  private String weather;       // 구름 많음, 맑음 등
  private String icon;          // 아이콘 URL
  private double temperature;
  private String description;
}