package com.example.iot_dashboard.service;

import com.example.iot_dashboard.model.DeviceData;
import com.example.iot_dashboard.repository.DeviceDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class DeviceDataService {

    @Autowired
    private DeviceDataRepository deviceDataRepository;

    // Enregistrer les données du dispositif
    public DeviceData syncDeviceData(String deviceId, int steps, double distance, int caloriesBurned, int heartRate) {
        DeviceData deviceData = new DeviceData();
        deviceData.setDeviceId(deviceId);
        deviceData.setSteps(steps);
        deviceData.setDistance(distance);
        deviceData.setCaloriesBurned(caloriesBurned);
        deviceData.setHeartRate(heartRate);
        deviceData.setDate(LocalDateTime.now());  // Utiliser LocalDateTime pour capturer la date et l'heure actuelles

        return deviceDataRepository.save(deviceData);
    }

    public List<DeviceData> getDeviceDataByDeviceId(String deviceId) {
        return deviceDataRepository.findByDeviceId(deviceId);
    }

    // Récupérer les données d'un dispositif pour une période donnée
    public List<DeviceData> getDeviceDataByDeviceIdAndDateRange(String deviceId, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        // Interroger MongoDB pour trouver les données dans la plage de dates spécifiée
        return deviceDataRepository.findByDeviceIdAndDateBetween(deviceId, startDateTime, endDateTime);
    }


}
