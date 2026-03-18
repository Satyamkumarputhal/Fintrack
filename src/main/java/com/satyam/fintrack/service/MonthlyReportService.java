package com.satyam.fintrack.service;
import com.satyam.fintrack.entity.User;


import com.satyam.fintrack.dto.SavingsEstimateDTO;
import com.satyam.fintrack.dto.TopCategoryDTO;
import com.satyam.fintrack.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MonthlyReportService {

    private final UserRepository userRepository;
    private final AnalyticsService analyticsService;
    private final EmailService emailService;

    public void sendMonthlyReports() {

        int year = LocalDate.now().minusMonths(1).getYear();
        int month = LocalDate.now().minusMonths(1).getMonthValue();

        List<User> users = userRepository.findAll();

        for (User user : users) {

            Long userId = user.getId();

            BigDecimal totalSpent =
                    analyticsService.getMonthlyTotalForUser(userId, year, month);

            TopCategoryDTO topCategory =
                    analyticsService.getTopCategoryForUser(userId, year, month);

            SavingsEstimateDTO savings =
                    analyticsService.getSavingsEstimateForUser(userId, year, month);

            StringBuilder emailBody = new StringBuilder();

            emailBody.append("Monthly Financial Report\n\n");

            emailBody.append("Total Spent: ")
                    .append(totalSpent)
                    .append("\n");

            if (topCategory != null) {
                emailBody.append("Top Category: ")
                        .append(topCategory.getCategory())
                        .append(" (")
                        .append(topCategory.getAmount())
                        .append(")\n");
            }

            emailBody.append("Estimated Savings: ")
                    .append(savings.getEstimatedSavings());

            emailService.sendEmail(
                    user.getEmail(),
                    "Monthly Financial Report",
                    emailBody.toString()
            );
        }
    }
}
