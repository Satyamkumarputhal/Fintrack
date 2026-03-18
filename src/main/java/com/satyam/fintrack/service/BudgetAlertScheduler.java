package com.satyam.fintrack.service;

import com.satyam.fintrack.dto.BudgetSummaryResponse;
import com.satyam.fintrack.entity.Budget;
import com.satyam.fintrack.entity.User;
import com.satyam.fintrack.repository.BudgetRepository;
import com.satyam.fintrack.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BudgetAlertScheduler {

    private final BudgetService budgetService;
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final BudgetRepository budgetRepository;

    @Scheduled(cron = "0 0 * * * ?") // every minute for testing
    public void checkBudgetAlerts() {

        System.out.println("Running Budget Alert Check...");

        int year = LocalDate.now().getYear();
        int month = LocalDate.now().getMonthValue();

        List<User> users = userRepository.findAll();

        for (User user : users) {

            List<BudgetSummaryResponse> summaries =
                    budgetService.getBudgetSummaryForUser(
                            user.getId(),
                            year,
                            month
                    );

            for (BudgetSummaryResponse summary : summaries) {

                if ("WARNING".equals(summary.status()) ||
                        "EXCEEDED".equals(summary.status())) {

                    Budget budget = budgetRepository
                            .findByUserIdAndCategoryNameAndYearAndMonth(
                                    user.getId(),
                                    summary.category(),
                                    year,
                                    month
                            )
                            .orElse(null);

                    if (budget == null) {
                        continue;
                    }

                    String lastStatus = budget.getLastAlertStatus();

                    if (!summary.status().equals(lastStatus)) {

                        System.out.println(
                                "Sending alert to: " + user.getEmail() +
                                        " | Category: " + summary.category() +
                                        " | Status: " + summary.status()
                        );

                        String message = """
                                Budget Alert!

                                Category: %s
                                Budget: %s
                                Spent: %s
                                Remaining: %s
                                Status: %s
                                """.formatted(
                                summary.category(),
                                summary.budget(),
                                summary.spent(),
                                summary.remaining(),
                                summary.status()
                        );

                        emailService.sendEmail(
                                user.getEmail(),
                                "FinTrack Budget Alert",
                                message
                        );

                        budget.setLastAlertStatus(summary.status());
                        budgetRepository.save(budget);
                    }
                }
            }
        }
    }
}