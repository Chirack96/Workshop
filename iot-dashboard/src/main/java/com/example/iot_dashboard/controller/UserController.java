package com.example.iot_dashboard.controller;

import com.example.iot_dashboard.model.Device;
import com.example.iot_dashboard.model.User;
import com.example.iot_dashboard.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.saveUser(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{id}/devices")
    public ResponseEntity<List<Device>> getUserDevices(@PathVariable String id) {
        List<Device> devices = userService.getUserDevices(id);
        return ResponseEntity.ok(devices);
    }

    @GetMapping("/{id}/active-device")
    public ResponseEntity<Device> getActiveDeviceForUser(@PathVariable String id) {
        Device activeDevice = userService.getActiveDeviceForUser(id);
        return ResponseEntity.ok(activeDevice);
    }

    @PostMapping("/{userId}/devices")
    public ResponseEntity<Device> addDeviceToUser(@PathVariable String userId, @RequestBody Device device) {
        Device savedDevice = userService.addDeviceToUser(userId, device);
        return ResponseEntity.ok(savedDevice);
    }


    @PutMapping("/{userId}/devices/{deviceId}/activate")
    public ResponseEntity<Device> activateDeviceForUser(@PathVariable String userId,
                                                        @PathVariable String deviceId,
                                                        @RequestBody Map<String, Boolean> isActive) {
        // Extraire l'état envoyé par le frontend
        boolean activeState = isActive.get("isActive");

        // Mettre à jour l'appareil dans la base de données en fonction de cet état
        Device updatedDevice = userService.activateDeviceForUser(userId, deviceId, activeState);

        return ResponseEntity.ok(updatedDevice);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    //delete all users

    @DeleteMapping
    public ResponseEntity<Void> deleteAllUsers() {
        userService.deleteAllUsers();
        return ResponseEntity.noContent().build();
    }
}
