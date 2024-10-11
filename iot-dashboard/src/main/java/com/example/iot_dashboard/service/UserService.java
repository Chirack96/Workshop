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

import java.util.ArrayList;
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
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    // Update a user
    public User updateUser(String id, User user) {
        user.setId(id);  // Ensure the ID is set
        return userRepository.save(user);
    }

    // Add a new device to a user
    public Device addDeviceToUser(String userId, Device device) {
        // Récupérer l'utilisateur par son ID
        User user = getUserById(userId);

        // Si l'utilisateur n'a pas encore de liste de devices, en créer une
        if (user.getDevices() == null) {
            user.setDevices(new ArrayList<>());
        }

        // Toujours définir isActive à false lors de la création de l'appareil
        device.setActive(false);

        // Ajouter le nouvel appareil à la liste de l'utilisateur
        user.getDevices().add(device);

        // Sauvegarder l'utilisateur avec les nouvelles données
        saveUser(user);

        return device;
    }



    // Activate a device for a user (deactivate others)
    public Device activateDeviceForUser(String userId, String deviceId, boolean isActive) {
        // Rechercher l'appareil correspondant dans la base de données
        Device device = (Device) deviceRepository.findByIdAndUserId(deviceId, userId)
                .orElseThrow(() -> new RuntimeException("Appareil non trouvé pour cet utilisateur"));

        // Mettre à jour l'état de l'appareil
        device.setActive(isActive);

        // Enregistrer l'appareil mis à jour dans la base de données
        return deviceRepository.save(device);
    }

    // Delete a user
    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

    public List<Device> getUserDevices(String id) {
        return deviceRepository.findByUserId(id);
    }

    public Device getActiveDeviceForUser(String id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return user.getActiveDevice();
        }
        return null;
    }

    //delete all users
    public void deleteAllUsers() {
        userRepository.deleteAll();
    }
}
