package com.example.iot_dashboard.repository;

import com.example.iot_dashboard.model.UserStatsHistory;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;

public interface UserStatsHistoryRepository extends MongoRepository<UserStatsHistory, String> {
    // Récupérer les statistiques pour une date spécifique et un type de période
    List<UserStatsHistory> findByUserIdAndDateAndPeriodType(String userId, LocalDate date, String periodType);

    // Récupérer les statistiques pour une plage de dates et un type de période (ex: semaine, mois)
    List<UserStatsHistory> findByUserIdAndDateBetweenAndPeriodType(String userId, LocalDate startDate, LocalDate endDate, String periodType);
}
