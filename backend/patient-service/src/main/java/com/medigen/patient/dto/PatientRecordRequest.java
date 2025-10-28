package com.medigen.patient.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientRecordRequest {
    
    @NotNull(message = "Patient ID is required")
    private Long patientId;
    
    @NotBlank(message = "Symptoms are required")
    private String symptoms;
    
    @NotBlank(message = "Description is required")
    private String description;
    
    private String medicalHistory;
    private String currentMedications;
    private String allergies;
    private String vitalSigns;
}
