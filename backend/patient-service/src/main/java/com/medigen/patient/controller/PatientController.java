package com.medigen.patient.controller;

import com.medigen.patient.dto.PatientRegistrationRequest;
import com.medigen.patient.dto.PatientRecordRequest;
import com.medigen.patient.model.PatientRecord;
import com.medigen.patient.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class PatientController {
    
    private final PatientService patientService;
    
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerPatient(@Valid @RequestBody PatientRegistrationRequest request) {
        try {
            Map<String, Object> response = patientService.registerPatient(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @PostMapping("/records")
    public ResponseEntity<Map<String, Object>> createPatientRecord(@Valid @RequestBody PatientRecordRequest request) {
        try {
            Map<String, Object> response = patientService.createPatientRecord(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/records")
    public ResponseEntity<?> getAllPatientRecords() {
        try {
            List<PatientRecord> records = patientService.getAllPatientRecords();
            return ResponseEntity.ok(records);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of(
                "error", "Failed to fetch records",
                "message", e.getMessage(),
                "details", e.getClass().getName()
            ));
        }
    }
    
    @GetMapping("/{patientId}/records")
    public ResponseEntity<?> getPatientRecords(@PathVariable Long patientId) {
        try {
            List<PatientRecord> records = patientService.getPatientRecordsByPatientId(patientId);
            return ResponseEntity.ok(records);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of(
                "error", "Failed to fetch patient records",
                "message", e.getMessage()
            ));
        }
    }
    
    @GetMapping("/records/doctor/{doctorId}")
    public ResponseEntity<?> getDoctorPatientRecords(@PathVariable Long doctorId) {
        try {
            List<PatientRecord> records = patientService.getPatientRecordsByDoctorId(doctorId);
            return ResponseEntity.ok(records);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of(
                "error", "Failed to fetch doctor's patient records",
                "message", e.getMessage()
            ));
        }
    }
    
    @PutMapping("/records/{recordId}")
    public ResponseEntity<?> updatePatientRecord(
            @PathVariable("recordId") Long recordId,
            @RequestParam(value = "doctorNotes", required = false) String doctorNotes,
            @RequestParam(value = "doctorId", required = false) Long doctorId) {
        try {
            System.out.println("Updating record " + recordId + " with notes: " + doctorNotes + ", doctorId: " + doctorId);
            PatientRecord updatedRecord = patientService.updatePatientRecord(recordId, doctorNotes, doctorId);
            return ResponseEntity.ok(updatedRecord);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of(
                "error", "Failed to update record",
                "message", e.getMessage(),
                "details", e.getClass().getName()
            ));
        }
    }
    
    @GetMapping("/{patientId}")
    public ResponseEntity<Map<String, Object>> getPatient(@PathVariable Long patientId) {
        try {
            var patient = patientService.getPatientById(patientId);
            return ResponseEntity.ok(Map.of(
                "id", patient.getId(),
                "firstName", patient.getFirstName(),
                "lastName", patient.getLastName(),
                "email", patient.getEmail(),
                "age", patient.getAge(),
                "gender", patient.getGender()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
