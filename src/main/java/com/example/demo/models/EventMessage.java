package com.example.demo.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

/**
 * The purpose of this class is to encapsulate
 * the event payload and event metadata
 */
@Data
@ToString
@NoArgsConstructor
public class EventMessage {
    private String eventId;
    private String eventType;
    private String correlationId;
    private Date createDate = new Date();
    private String createdBy;
    private String payload;
}
