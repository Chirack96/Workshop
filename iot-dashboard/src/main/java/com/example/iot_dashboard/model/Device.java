package com.example.iot_dashboard.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "devices")
public class Device {

    @Id
    private String id;

    @NotBlank
    private String userId;  // ID de l'utilisateur auquel ce dispositif est associé

    @NotBlank
    private String name;

    @NotBlank
    private String type;

    private boolean isActive;  // Indique si ce dispositif est actif ou non

    public void setActive(boolean active) {
        this.isActive = active;
    }
}
