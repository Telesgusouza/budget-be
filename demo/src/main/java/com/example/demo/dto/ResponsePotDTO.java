package com.example.demo.dto;

import java.time.Instant;

public record ResponsePotDTO(String title, String description, 
		Float monthlyAmount,Instant lastUpdate) {

}
