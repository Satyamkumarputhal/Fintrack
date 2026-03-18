package com.satyam.fintrack.scheduler;

import com.satyam.fintrack.service.WeeklySummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WeeklySummaryScheduler {

    private final WeeklySummaryService weeklySummaryService;

    @Scheduled(cron = "0 0 9 ? * SUN")
    public void runWeeklySummary() {

        weeklySummaryService.sendWeeklySummary();
    }
}
