package com.example.iot_dashboard.service;

import com.example.iot_dashboard.model.Device;
import com.example.iot_dashboard.model.User;
import com.example.iot_dashboard.repository.DeviceRepository;
import com.example.iot_dashboard.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    // Récupérer tous les utilisateurs avec leurs dispositifs associés
    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();

        // Charger les dispositifs pour chaque utilisateur
        for (User user : users) {
            List<Device> devices = deviceRepository.findByUserId(user.getId());
            user.setDevices(devices);
        }

        return users;
    }

    // Récupérer un utilisateur par ID avec ses dispositifs associés
    public User getUserById(String id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            List<Device> devices = deviceRepository.findByUserId(user.getId());
            user.setDevices(devices);
            return user;
        }
        return null;
    }

    // Créer un utilisateur
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    // Mettre à jour un utilisateur
    public User updateUser(String id, User user) {
        user.setId(id);  // S'assurer que l'ID est bien défini
        return userRepository.save(user);
    }

    // Ajouter un nouveau device pour un utilisateur
    public Device addDeviceToUser(String userId, Device device) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            device.setUserId(userId);
            return deviceRepository.save(device);  // Sauvegarder le device
        } else {
            throw new RuntimeException("User not found");
        }
    }

    // Activer un device pour un utilisateur (désactive les autres)
    public Device activateDeviceForUser(String userId, String deviceId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            List<Device> devices = deviceRepository.findByUserId(userId);

            for (Device device : devices) {
                if (device.getId().equals(deviceId)) {
                    device.setActive(true);  // Activer ce device
                } else {
                    device.setActive(false);  // Désactiver les autres
                }
                deviceRepository.save(device);  // Mettre à jour chaque device
            }

            return deviceRepository.findById(deviceId).orElse(null);  // Retourner le device activé
        } else {
            throw new RuntimeException("User not found");
        }
    }

    // Supprimer un utilisateur
    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }
}
