package com.medigen.patient.client;

import com.medigen.patient.dto.PatientRecordRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "ai-service")
public interface AiServiceClient {
    
    @PostMapping("/api/ai/generate-notes")
    Map<String, Object> generateClinicalNotes(@RequestBody PatientRecordRequest request);
}
