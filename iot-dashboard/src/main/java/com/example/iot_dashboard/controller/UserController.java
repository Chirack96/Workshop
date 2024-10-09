package com.example.iot_dashboard.controller;

import com.example.iot_dashboard.model.Device;
import com.example.iot_dashboard.model.User;
import com.example.iot_dashboard.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping("/{userId}/devices")
    public ResponseEntity<Device> addDeviceToUser(@PathVariable String userId, @RequestBody Device device) {
        Device savedDevice = userService.addDeviceToUser(userId, device);
        return ResponseEntity.ok(savedDevice);
    }

    @PutMapping("/{userId}/devices/{deviceId}/activate")
    public ResponseEntity<Device> activateDeviceForUser(@PathVariable String userId, @PathVariable String deviceId) {
        Device activatedDevice = userService.activateDeviceForUser(userId, deviceId);
        return ResponseEntity.ok(activatedDevice);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
