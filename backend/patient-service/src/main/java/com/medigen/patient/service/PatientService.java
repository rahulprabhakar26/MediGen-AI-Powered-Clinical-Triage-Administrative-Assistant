package com.medigen.patient.service;

import com.medigen.patient.dto.PatientRegistrationRequest;
import com.medigen.patient.dto.PatientRecordRequest;
import com.medigen.patient.model.Patient;
import com.medigen.patient.model.PatientRecord;
import com.medigen.patient.repository.PatientRepository;
import com.medigen.patient.repository.PatientRecordRepository;
import com.medigen.patient.client.AiServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PatientService {
    
    private final PatientRepository patientRepository;
    private final PatientRecordRepository patientRecordRepository;
    private final AiServiceClient aiServiceClient;
    
    public Map<String, Object> registerPatient(PatientRegistrationRequest request) {
        if (patientRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        
        Patient patient = new Patient();
        patient.setFirstName(request.getFirstName());
        patient.setLastName(request.getLastName());
        patient.setEmail(request.getEmail());
        patient.setPhoneNumber(request.getPhoneNumber());
        patient.setAge(request.getAge());
        patient.setGender(request.getGender());
        patient.setAddress(request.getAddress());
        patient.setEmergencyContact(request.getEmergencyContact());
        patient.setEmergencyPhone(request.getEmergencyPhone());
        patient.setCreatedAt(new Date());
        
        Patient savedPatient = patientRepository.save(patient);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Patient registered successfully");
        response.put("patientId", savedPatient.getId());
        response.put("email", savedPatient.getEmail());
        
        return response;
    }
    
    public Map<String, Object> createPatientRecord(PatientRecordRequest request) {
        Patient patient = patientRepository.findById(request.getPatientId())
            .orElseThrow(() -> new RuntimeException("Patient not found"));
        
        PatientRecord record = new PatientRecord();
        record.setPatient(patient);
        record.setSymptoms(request.getSymptoms());
        record.setDescription(request.getDescription());
        record.setMedicalHistory(request.getMedicalHistory());
        record.setCurrentMedications(request.getCurrentMedications());
        record.setAllergies(request.getAllergies());
        record.setVitalSigns(request.getVitalSigns());
        record.setCreatedAt(new Date());
        
        PatientRecord savedRecord = patientRecordRepository.save(record);
        
        // Call AI service to generate notes
        try {
            Map<String, Object> aiResponse = aiServiceClient.generateClinicalNotes(request);
            savedRecord.setAiGeneratedNotes((String) aiResponse.get("notes"));
            savedRecord.setTriageLevel(PatientRecord.TriageLevel.valueOf((String) aiResponse.get("triageLevel")));
            savedRecord.setUpdatedAt(new Date());
            savedRecord = patientRecordRepository.save(savedRecord);
        } catch (Exception e) {
            // Log error but don't fail the request
            System.err.println("AI service call failed: " + e.getMessage());
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Patient record created successfully");
        response.put("recordId", savedRecord.getId());
        response.put("triageLevel", savedRecord.getTriageLevel());
        response.put("aiGeneratedNotes", savedRecord.getAiGeneratedNotes());
        
        return response;
    }
    
    public List<PatientRecord> getAllPatientRecords() {
        try {
            List<PatientRecord> records = patientRecordRepository.findAll();
            System.out.println("Found " + records.size() + " patient records");
            return records;
        } catch (Exception e) {
            System.err.println("Error fetching patient records: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    public List<PatientRecord> getPatientRecordsByPatientId(Long patientId) {
        return patientRecordRepository.findByPatientId(patientId);
    }
    
    public List<PatientRecord> getPatientRecordsByDoctorId(Long doctorId) {
        return patientRecordRepository.findByDoctorId(doctorId);
    }
    
    public PatientRecord updatePatientRecord(Long recordId, String doctorNotes, Long doctorId) {
        PatientRecord record = patientRecordRepository.findById(recordId)
            .orElseThrow(() -> new RuntimeException("Patient record not found"));
        
        if (doctorNotes != null && !doctorNotes.trim().isEmpty()) {
            record.setDoctorNotes(doctorNotes);
        }
        if (doctorId != null) {
            record.setDoctorId(doctorId);
        }
        record.setUpdatedAt(new Date());
        
        return patientRecordRepository.save(record);
    }
    
    public Patient getPatientById(Long patientId) {
        return patientRepository.findById(patientId)
            .orElseThrow(() -> new RuntimeException("Patient not found"));
    }
}
