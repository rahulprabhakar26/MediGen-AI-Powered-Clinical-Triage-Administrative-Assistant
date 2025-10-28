package com.medigen.patient.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "patient_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientRecord {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Patient patient;
    
    @Column(nullable = false)
    private String symptoms;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(columnDefinition = "TEXT")
    private String medicalHistory;
    
    @Column(columnDefinition = "TEXT")
    private String currentMedications;
    
    @Column(columnDefinition = "TEXT")
    private String allergies;
    
    @Column(columnDefinition = "TEXT")
    private String vitalSigns;
    
    @Column(columnDefinition = "TEXT")
    private String aiGeneratedNotes;
    
    @Enumerated(EnumType.STRING)
    private TriageLevel triageLevel;
    
    @Column(nullable = false)
    private Date createdAt = new Date();
    
    @Column
    private Date updatedAt;
    
    @Column
    private Long doctorId;
    
    @Column(columnDefinition = "TEXT")
    private String doctorNotes;
    
    public enum TriageLevel {
        LOW, MEDIUM, HIGH
    }
}
