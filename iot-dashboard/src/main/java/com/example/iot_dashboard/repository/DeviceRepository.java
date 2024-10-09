package com.example.iot_dashboard.repository;

import com.example.iot_dashboard.model.Device;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DeviceRepository extends MongoRepository<Device, String> {
    // Trouver tous les dispositifs associés à un utilisateur donné
    List<Device> findByUserId(String userId);
}
