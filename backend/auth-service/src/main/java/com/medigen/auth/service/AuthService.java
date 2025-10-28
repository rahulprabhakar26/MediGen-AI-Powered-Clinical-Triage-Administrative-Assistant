package com.medigen.auth.service;

import com.medigen.auth.dto.DoctorRegistrationRequest;
import com.medigen.auth.dto.LoginRequest;
import com.medigen.auth.model.Doctor;
import com.medigen.auth.repository.DoctorRepository;
import com.medigen.auth.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final DoctorRepository doctorRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    
    public Map<String, Object> registerDoctor(DoctorRegistrationRequest request) {
        if (doctorRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        
        if (doctorRepository.existsByLicenseNumber(request.getLicenseNumber())) {
            throw new RuntimeException("License number already exists");
        }
        
        Doctor doctor = new Doctor();
        doctor.setEmail(request.getEmail());
        doctor.setPassword(passwordEncoder.encode(request.getPassword()));
        doctor.setFirstName(request.getFirstName());
        doctor.setLastName(request.getLastName());
        doctor.setSpecialization(request.getSpecialization());
        doctor.setLicenseNumber(request.getLicenseNumber());
        doctor.setPhoneNumber(request.getPhoneNumber());
        doctor.setCreatedAt(new Date());
        
        Doctor savedDoctor = doctorRepository.save(doctor);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Doctor registered successfully");
        response.put("doctorId", savedDoctor.getId());
        response.put("email", savedDoctor.getEmail());
        
        return response;
    }
    
    public Map<String, Object> loginDoctor(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        
        Doctor doctor = doctorRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new RuntimeException("Doctor not found"));
        
        doctor.setLastLogin(new Date());
        doctorRepository.save(doctor);
        
        String token = jwtUtil.generateToken(doctor.getEmail());
        
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("doctorId", doctor.getId());
        response.put("email", doctor.getEmail());
        response.put("firstName", doctor.getFirstName());
        response.put("lastName", doctor.getLastName());
        response.put("specialization", doctor.getSpecialization());
        
        return response;
    }
    
    public Doctor getDoctorByEmail(String email) {
        return doctorRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Doctor not found"));
    }
}
