package com.satyam.fintrack.service;

import com.satyam.fintrack.dto.CategoryBreakdownDTO;
import com.satyam.fintrack.entity.User;
import com.satyam.fintrack.repository.ExpenseRepository;
import com.satyam.fintrack.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WeeklySummaryService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    public void sendWeeklySummary() {

        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(7);

        List<User> users = userRepository.findAll();

        for (User user : users) {

            List<CategoryBreakdownDTO> summary =
                    expenseRepository.getWeeklyBreakdown(
                            user.getId(),
                            startDate,
                            endDate
                    );

            StringBuilder emailBody = new StringBuilder();
            emailBody.append("Weekly Expense Summary\n\n");

            double total = 0;

            for (CategoryBreakdownDTO item : summary) {

                emailBody.append(item.getCategory())
                        .append(": ")
                        .append(item.getTotal())
                        .append("\n");

                total += item.getTotal().doubleValue();
            }

            emailBody.append("\nTotal: ").append(total);

            emailService.sendEmail(
                    user.getEmail(),
                    "Weekly Expense Summary",
                    emailBody.toString()
            );
        }
    }
}
