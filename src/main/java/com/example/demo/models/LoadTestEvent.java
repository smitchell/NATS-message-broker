package com.example.demo.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoadTestEvent {
    private long id;
    private String message;
}
