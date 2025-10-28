package com.medigen.ai.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "ai_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AiLog {
    
    @Id
    private String id;
    
    private String patientId;
    private String symptoms;
    private String description;
    private String prompt;
    private String response;
    private String triageLevel;
    private LocalDateTime timestamp;
    private String status; // SUCCESS, FAILED
    private String errorMessage;
}
