package com.example.iot_dashboard.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "device_data")
public class DeviceData {

    @Id
    private String id;

    private String deviceId;  // ID du dispositif auquel ces données sont associées
    private int steps;
    private double distance;
    private int caloriesBurned;
    private int heartRate;

    private LocalDateTime date;  // Date à laquelle ces données ont été enregistrées
}
