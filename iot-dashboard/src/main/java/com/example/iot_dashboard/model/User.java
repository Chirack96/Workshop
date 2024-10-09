package com.example.iot_dashboard.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User {

    @Id
    private String id;
    private String name;
    private String email;

    // Liste des devices de l'utilisateur
    @DBRef
    private List<Device> devices;

    // Méthode pour récupérer le device actif
    public Device getActiveDevice() {
        if (devices != null) {
            return devices.stream()
                    .filter(Device::isActive)
                    .findFirst()
                    .orElse(null);  // Retourne null si aucun dispositif actif
        }
        return null;
    }
}
