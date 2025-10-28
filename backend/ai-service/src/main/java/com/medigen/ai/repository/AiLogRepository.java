package com.medigen.ai.repository;

import com.medigen.ai.model.AiLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AiLogRepository extends MongoRepository<AiLog, String> {
    List<AiLog> findByPatientId(String patientId);
    List<AiLog> findByStatus(String status);
}
