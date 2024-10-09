package com.example.iot_dashboard.model;

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

    private String userId;  // ID de l'utilisateur auquel ce dispositif est associé
    private String name;

    private boolean active;  // Indique si ce dispositif est actif ou non
}
