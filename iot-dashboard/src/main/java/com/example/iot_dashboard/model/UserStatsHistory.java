package com.example.iot_dashboard.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "userStatsHistory")
public class UserStatsHistory {

    @Id
    private String id;

    private String userId; // Ce champ est nécessaire pour les requêtes par utilisateur
    private LocalDate date;  // Date du résumé (journalier, hebdomadaire, mensuel)
    private int totalSteps;
    private double totalDistance;
    private int totalCaloriesBurned;
    private int averageHeartRate;
    private String periodType;  // "DAILY", "WEEKLY", "MONTHLY"
}
