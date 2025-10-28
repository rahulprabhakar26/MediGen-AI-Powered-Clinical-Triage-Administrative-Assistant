package com.medigen.ai.controller;

import com.medigen.ai.dto.PatientRecordRequest;
import com.medigen.ai.service.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class AiController {
    
    private final AiService aiService;
    
    @PostMapping("/generate-notes")
    public ResponseEntity<Map<String, Object>> generateClinicalNotes(@RequestBody PatientRecordRequest request) {
        try {
            Map<String, Object> response = aiService.generateClinicalNotes(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
