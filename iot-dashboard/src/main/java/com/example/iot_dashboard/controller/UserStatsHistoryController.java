package com.example.iot_dashboard.controller;

import com.example.iot_dashboard.model.UserStatsHistory;
import com.example.iot_dashboard.service.UserStatsHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@RestController
@RequestMapping("/api/users/{userId}/stats")
public class UserStatsHistoryController {

    @Autowired
    private UserStatsHistoryService userStatsHistoryService;

    // Récupérer les statistiques journalières
    @GetMapping("/daily")
    public List<UserStatsHistory> getDailyStats(@PathVariable String userId, @RequestParam("date") String date) {
        LocalDate parsedDate = LocalDate.parse(date);
        return userStatsHistoryService.getDailyStats(userId, parsedDate);
    }

    // Récupérer les statistiques hebdomadaires
    @GetMapping("/weekly")
    public List<UserStatsHistory> getWeeklyStats(@PathVariable String userId, @RequestParam("date") String date) {
        LocalDate startOfWeek = LocalDate.parse(date).with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
        return userStatsHistoryService.getWeeklyStats(userId, startOfWeek);
    }

    // Récupérer les statistiques mensuelles
    @GetMapping("/monthly")
    public List<UserStatsHistory> getMonthlyStats(@PathVariable String userId, @RequestParam("date") String date) {
        LocalDate startOfMonth = LocalDate.parse(date).with(TemporalAdjusters.firstDayOfMonth());
        return userStatsHistoryService.getMonthlyStats(userId, startOfMonth);
    }
}
