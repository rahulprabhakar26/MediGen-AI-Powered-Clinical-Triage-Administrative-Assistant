package com.medigen.ai.service;

import com.medigen.ai.dto.GeminiRequest;
import com.medigen.ai.dto.GeminiResponse;
import com.medigen.ai.dto.PatientRecordRequest;
import com.medigen.ai.model.AiLog;
import com.medigen.ai.repository.AiLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AiService {
    
    private final WebClient.Builder webClientBuilder;
    private final AiLogRepository aiLogRepository;
    
    @Value("${gemini.api.key}")
    private String geminiApiKey;
    
    @Value("${gemini.api.url}")
    private String geminiApiUrl;
    
    public Map<String, Object> generateClinicalNotes(PatientRecordRequest request) {
        String prompt = buildPrompt(request);
        
        AiLog aiLog = new AiLog();
        aiLog.setPatientId(request.getPatientId().toString());
        aiLog.setSymptoms(request.getSymptoms());
        aiLog.setDescription(request.getDescription());
        aiLog.setPrompt(prompt);
        aiLog.setTimestamp(LocalDateTime.now());
        
        try {
            GeminiRequest geminiRequest = createGeminiRequest(prompt);
            
            WebClient webClient = webClientBuilder.build();
            
            GeminiResponse response = webClient.post()
                .uri(geminiApiUrl)
                .header("x-goog-api-key", geminiApiKey)
                .header("Content-Type", "application/json")
                .bodyValue(geminiRequest)
                .retrieve()
                .bodyToMono(GeminiResponse.class)
                .block();
            
            if (response != null && response.getCandidates() != null && !response.getCandidates().isEmpty()) {
                String aiResponse = response.getCandidates().get(0).getContent().getParts().get(0).getText();
                
                aiLog.setResponse(aiResponse);
                aiLog.setStatus("SUCCESS");
                
                Map<String, Object> result = parseAiResponse(aiResponse);
                aiLog.setTriageLevel((String) result.get("triageLevel"));
                
                aiLogRepository.save(aiLog);
                
                return result;
            } else {
                throw new RuntimeException("No response from Gemini");
            }
            
        } catch (Exception e) {
            aiLog.setStatus("FAILED");
            aiLog.setErrorMessage(e.getMessage());
            aiLogRepository.save(aiLog);
            
            // Return fallback response
            return createFallbackResponse(request);
        }
    }
    
    private GeminiRequest createGeminiRequest(String prompt) {
        GeminiRequest.Part part = new GeminiRequest.Part();
        part.setText(prompt);
        
        GeminiRequest.Content content = new GeminiRequest.Content();
        content.setParts(List.of(part));
        
        GeminiRequest.GenerationConfig config = new GeminiRequest.GenerationConfig();
        config.setTemperature(0.3);
        config.setMaxOutputTokens(500);
        
        GeminiRequest request = new GeminiRequest();
        request.setContents(List.of(content));
        request.setGenerationConfig(config);
        
        return request;
    }
    
    private String buildPrompt(PatientRecordRequest request) {
        return String.format("""
            Convert the following patient information into a structured SOAP note and suggest a triage category (LOW, MEDIUM, HIGH).
            
            Patient Information:
            - Symptoms: %s
            - Description: %s
            - Medical History: %s
            - Current Medications: %s
            - Allergies: %s
            - Vital Signs: %s
            
            Please provide the response in the following JSON format:
            {
                "subjective": "Patient's chief complaint and history",
                "objective": "Physical examination findings and vital signs",
                "assessment": "Clinical impression and diagnosis",
                "plan": "Treatment plan and recommendations",
                "triageLevel": "LOW/MEDIUM/HIGH"
            }
            """, 
            request.getSymptoms(),
            request.getDescription(),
            request.getMedicalHistory() != null ? request.getMedicalHistory() : "Not provided",
            request.getCurrentMedications() != null ? request.getCurrentMedications() : "None",
            request.getAllergies() != null ? request.getAllergies() : "None known",
            request.getVitalSigns() != null ? request.getVitalSigns() : "Not measured"
        );
    }
    
    private Map<String, Object> parseAiResponse(String aiResponse) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // Simple JSON parsing (in production, use proper JSON parser)
            String cleanResponse = aiResponse.trim();
            if (cleanResponse.startsWith("```json")) {
                cleanResponse = cleanResponse.substring(7);
            }
            if (cleanResponse.endsWith("```")) {
                cleanResponse = cleanResponse.substring(0, cleanResponse.length() - 3);
            }
            
            // Extract triage level
            String triageLevel = "MEDIUM"; // Default
            if (cleanResponse.contains("\"triageLevel\": \"HIGH\"")) {
                triageLevel = "HIGH";
            } else if (cleanResponse.contains("\"triageLevel\": \"LOW\"")) {
                triageLevel = "LOW";
            }
            
            result.put("notes", cleanResponse);
            result.put("triageLevel", triageLevel);
            
        } catch (Exception e) {
            result.put("notes", aiResponse);
            result.put("triageLevel", "MEDIUM");
        }
        
        return result;
    }
    
    private Map<String, Object> createFallbackResponse(PatientRecordRequest request) {
        Map<String, Object> result = new HashMap<>();
        
        String fallbackNotes = String.format("""
            {
                "subjective": "Patient presents with %s. %s",
                "objective": "Patient appears stable. Vital signs: %s",
                "assessment": "Requires further evaluation based on presenting symptoms",
                "plan": "Recommend consultation with healthcare provider for proper assessment",
                "triageLevel": "MEDIUM"
            }
            """,
            request.getSymptoms(),
            request.getDescription(),
            request.getVitalSigns() != null ? request.getVitalSigns() : "Not measured"
        );
        
        result.put("notes", fallbackNotes);
        result.put("triageLevel", "MEDIUM");
        
        return result;
    }
}
