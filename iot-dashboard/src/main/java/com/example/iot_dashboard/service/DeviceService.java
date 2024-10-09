package com.example.iot_dashboard.service;

import com.example.iot_dashboard.model.Device;
import com.example.iot_dashboard.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeviceService {

    @Autowired
    private DeviceRepository deviceRepository;

    // Récupérer un dispositif par ID

    public Device getDeviceById(String id) {
        Optional<Device> deviceOptional = deviceRepository.findById(id);
        return deviceOptional.orElse(null);
    }

    // Créer un dispositif

    public Device saveDevice(Device device) {
        return deviceRepository.save(device);
    }

    // Mettre à jour un dispositif

    public Device updateDevice(String id, Device device) {
        device.setId(id);  // S'assurer que l'ID est bien défini
        return deviceRepository.save(device);
    }

    // Supprimer un dispositif

    public void deleteDevice(String id) {
        deviceRepository.deleteById(id);
    }

    public List<Device> getAllDevices() {
        return deviceRepository.findAll();
    }
}
