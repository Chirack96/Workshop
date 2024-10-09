package com.example.iot_dashboard.repository;

import com.example.iot_dashboard.model.DeviceData;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface DeviceDataRepository extends MongoRepository<DeviceData, String> {

    // Récupérer les données du dispositif dans une plage de dates
    List<DeviceData> findByDeviceIdAndDateBetween(String deviceId, LocalDateTime startDateTime, LocalDateTime endDateTime);

    // Récupérer les données du dispositif
    List<DeviceData> findByDeviceId(String deviceId);
}
