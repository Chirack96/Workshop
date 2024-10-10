package com.example.iot_dashboard.service;

import com.example.iot_dashboard.model.Device;
import com.example.iot_dashboard.model.User;
import com.example.iot_dashboard.repository.DeviceRepository;
import com.example.iot_dashboard.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final DeviceRepository deviceRepository;
    private final PasswordEncoder passwordEncoder;

    // Constructor-based injection to avoid circular dependencies
    public UserService(UserRepository userRepository, DeviceRepository deviceRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.deviceRepository = deviceRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Load user by email (required by Spring Security)
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        return userOptional.get();  // Ensure the user is returned as a UserDetails object
    }


    // Register a new user
    public User registerUser(User user) {
        // Ensure email uniqueness
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        // Encode password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }


    // Get all users with their associated devices
    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();

        // Load devices for each user
        for (User user : users) {
            List<Device> devices = deviceRepository.findByUserId(user.getId());
            user.setDevices(devices);
        }

        return users;
    }

    // Get user by ID with their associated devices
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

    // Save a user
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    // Update a user
    public User updateUser(String id, User user) {
        user.setId(id);  // Ensure the ID is set
        return userRepository.save(user);
    }

    // Add a new device to a user
    public Device addDeviceToUser(String userId, Device device) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            device.setUserId(userId);
            return deviceRepository.save(device);  // Save the device
        } else {
            throw new RuntimeException("User not found");
        }
    }

    // Activate a device for a user (deactivate others)
    public Device activateDeviceForUser(String userId, String deviceId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            List<Device> devices = deviceRepository.findByUserId(userId);

            for (Device device : devices) {
                if (device.getId().equals(deviceId)) {
                    device.setActive(true);  // Activate this device
                } else {
                    device.setActive(false);  // Deactivate the others
                }
                deviceRepository.save(device);  // Update each device
            }

            return deviceRepository.findById(deviceId).orElse(null);  // Return the activated device
        } else {
            throw new RuntimeException("User not found");
        }
    }

    // Delete a user
    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }
}
