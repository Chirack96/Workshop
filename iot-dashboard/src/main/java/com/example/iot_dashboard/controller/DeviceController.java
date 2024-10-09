package com.example.iot_dashboard.controller;

import com.example.iot_dashboard.model.Device;
import com.example.iot_dashboard.model.DeviceData;
import com.example.iot_dashboard.service.DeviceDataService;
import com.example.iot_dashboard.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/devices")
public class DeviceController {

    @Autowired
    private DeviceDataService deviceDataService;

    @Autowired
    private DeviceService deviceService;

    // Create a device
    @PostMapping
    public ResponseEntity<Device> createDevice(@RequestBody Device device) {
        Device createdDevice = deviceService.saveDevice(device);
        return ResponseEntity.ok(createdDevice);
    }

    @GetMapping
    public ResponseEntity<List<Device>> getAllDevices() {
        List<Device> devices = deviceService.getAllDevices();
        return ResponseEntity.ok(devices);
    }

    // Get a device by ID
    @GetMapping("/{id}")
    public ResponseEntity<Device> getDeviceById(@PathVariable String id) {
        Device device = deviceService.getDeviceById(id);
        return ResponseEntity.ok(device);
    }

    // Update a device
    @PutMapping("/{id}")
    public ResponseEntity<Device> updateDevice(@PathVariable String id, @RequestBody Device device) {
        Device updatedDevice = deviceService.updateDevice(id, device);
        return ResponseEntity.ok(updatedDevice);
    }

    // Delete a device
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDevice(@PathVariable String id) {
        deviceService.deleteDevice(id);
        return ResponseEntity.noContent().build();
    }

    // Synchroniser les données d'un dispositif
    @PostMapping("/{deviceId}/sync")
    public ResponseEntity<DeviceData> syncDeviceData(
            @PathVariable String deviceId,
            @RequestBody DeviceData deviceData
    ) {
        deviceData.setDeviceId(deviceId);
        DeviceData syncedData = deviceDataService.syncDeviceData(
                deviceId,
                deviceData.getSteps(),
                deviceData.getDistance(),
                deviceData.getCaloriesBurned(),
                deviceData.getHeartRate()
        );
        return ResponseEntity.ok(syncedData);
    }

    // Get device data by device ID
    @GetMapping("/{deviceId}/data")
    public ResponseEntity<List<DeviceData>> getDeviceDataByDeviceId(@PathVariable String deviceId) {
        List<DeviceData> deviceDataList = deviceDataService.getDeviceDataByDeviceId(deviceId);
        return ResponseEntity.ok(deviceDataList);
    }

    // Get device data by device ID and date range

    @GetMapping("/{deviceId}/data/{startDate}/{endDate}")
    public ResponseEntity<List<DeviceData>> getDeviceDataByDeviceIdAndDateRange(
            @PathVariable String deviceId,
            @PathVariable String startDate,  // On reçoit les dates sous forme de chaîne
            @PathVariable String endDate
    ) {
        // Convertir les chaînes en LocalDateTime, en supposant que les chaînes sont au format 'yyyy-MM-ddTHH:mm:ss'
        LocalDateTime startDateTime = LocalDateTime.parse(startDate);
        LocalDateTime endDateTime = LocalDateTime.parse(endDate);

        // Récupérer les données pour la plage de dates
        List<DeviceData> deviceDataList = deviceDataService.getDeviceDataByDeviceIdAndDateRange(
                deviceId, startDateTime, endDateTime);

        return ResponseEntity.ok(deviceDataList);
    }

}
