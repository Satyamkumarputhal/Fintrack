package com.satyam.fintrack.scheduler;

import com.satyam.fintrack.service.MonthlyReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MonthlyReportScheduler {

    private final MonthlyReportService monthlyReportService;

    @Scheduled(cron = "0 0 9 1 * ?")
    public void runMonthlyReport() {

        monthlyReportService.sendMonthlyReports();
    }
}
