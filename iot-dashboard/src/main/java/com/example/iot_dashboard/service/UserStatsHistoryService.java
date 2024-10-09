package com.example.iot_dashboard.service;

import com.example.iot_dashboard.model.DeviceData;
import com.example.iot_dashboard.model.UserStatsHistory;
import com.example.iot_dashboard.repository.UserStatsHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class UserStatsHistoryService {

    @Autowired
    private DeviceDataService deviceDataService;

    @Autowired
    private UserStatsHistoryRepository userStatsHistoryRepository;

    // Récupérer les statistiques journalières
    public List<UserStatsHistory> getDailyStats(String userId, LocalDate date) {
        return userStatsHistoryRepository.findByUserIdAndDateAndPeriodType(userId, date, "DAILY");
    }

    // Récupérer les statistiques hebdomadaires
    public List<UserStatsHistory> getWeeklyStats(String userId, LocalDate startOfWeek) {
        LocalDate endOfWeek = startOfWeek.plusDays(6);  // Fin de la semaine
        return userStatsHistoryRepository.findByUserIdAndDateBetweenAndPeriodType(userId, startOfWeek, endOfWeek, "WEEKLY");
    }

    // Récupérer les statistiques mensuelles
    public List<UserStatsHistory> getMonthlyStats(String userId, LocalDate startOfMonth) {
        LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());  // Fin du mois
        return userStatsHistoryRepository.findByUserIdAndDateBetweenAndPeriodType(userId, startOfMonth, endOfMonth, "MONTHLY");
    }

    // Calculer les statistiques journalières pour un utilisateur
    public void calculateDailyStats(String userId, LocalDate date) {
        List<DeviceData> deviceDataList = deviceDataService.getDeviceDataByDeviceIdAndDateRange(userId, date.atStartOfDay(), date.atStartOfDay());
        UserStatsHistory dailyStats = aggregateStats(deviceDataList, date, "DAILY");
        userStatsHistoryRepository.save(dailyStats);
    }

    // Agréger les statistiques pour une période donnée
    private UserStatsHistory aggregateStats(List<DeviceData> deviceDataList, LocalDate date, String periodType) {
        int totalSteps = 0;
        double totalDistance = 0;
        int totalCaloriesBurned = 0;
        int totalHeartRate = 0;
        int dataCount = deviceDataList.size();

        for (DeviceData deviceData : deviceDataList) {
            totalSteps += deviceData.getSteps();
            totalDistance += deviceData.getDistance();
            totalCaloriesBurned += deviceData.getCaloriesBurned();
            totalHeartRate += deviceData.getHeartRate();
        }

        // Moyenne de la fréquence cardiaque
        int averageHeartRate = dataCount > 0 ? totalHeartRate / dataCount : 0;

        return new UserStatsHistory(
                null,
                deviceDataList.isEmpty() ? null : deviceDataList.get(0).getDeviceId(),
                date,
                totalSteps,
                totalDistance,
                totalCaloriesBurned,
                averageHeartRate,
                periodType
        );
    }
}
