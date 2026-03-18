package com.satyam.fintrack.controller;

import com.satyam.fintrack.Security.CustomAccessDeniedHandler;
import com.satyam.fintrack.Security.JwtAuthenticationEntryPoint;
import com.satyam.fintrack.Security.JwtAuthenticationFilter;
import com.satyam.fintrack.Security.JwtService;
import com.satyam.fintrack.config.SecurityConfig;
import com.satyam.fintrack.controller.AnalyticsController;
import com.satyam.fintrack.exceptions.GlobalExceptionHandler;
import com.satyam.fintrack.exceptions.UserAlreadyAdminException;
import com.satyam.fintrack.service.AnalyticsService;
import com.satyam.fintrack.service.AdminService;
import com.satyam.fintrack.service.BudgetService;
import com.satyam.fintrack.service.ExpenseService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {
        AdminController.class,
        AnalyticsController.class,
        BudgetController.class,
        ExpenseController.class
})
@Import({
        SecurityConfig.class,
        JwtAuthenticationFilter.class,
        JwtAuthenticationEntryPoint.class,
        CustomAccessDeniedHandler.class,
        GlobalExceptionHandler.class
})
class ApiSecurityValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AdminService adminService;

    @MockitoBean
    private AnalyticsService analyticsService;

    @MockitoBean
    private BudgetService budgetService;

    @MockitoBean
    private ExpenseService expenseService;

    @MockitoBean
    private AuthenticationProvider authenticationProvider;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @Test
    void normalUserCannotPromoteUsers() throws Exception {
        mockMvc.perform(patch("/api/admin/users/5/promote")
                        .with(user("1").roles("USER")))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(403))
                .andExpect(jsonPath("$.messages[0]").value("Access Denied"))
                .andExpect(jsonPath("$.timestamp").exists());

        verifyNoInteractions(adminService);
    }

    @Test
    void createExpenseRejectsNegativeAmount() throws Exception {
        mockMvc.perform(post("/api/expenses")
                        .with(user("1").roles("USER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "amount": -50,
                                  "description": "bad",
                                  "expenseDate": "2026-03-10",
                                  "categoryId": 1
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.messages[0]").value("amount: must be greater than or equal to 0.01"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void createExpenseRejectsZeroAmount() throws Exception {
        mockMvc.perform(post("/api/expenses")
                        .with(user("1").roles("USER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "amount": 0,
                                  "description": "bad",
                                  "expenseDate": "2026-03-10",
                                  "categoryId": 1
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.messages[0]").value("amount: must be greater than or equal to 0.01"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void createExpenseRejectsInvalidDate() throws Exception {
        mockMvc.perform(post("/api/expenses")
                        .with(user("1").roles("USER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "amount": 50,
                                  "description": "bad date",
                                  "expenseDate": "2026-02-30",
                                  "categoryId": 1
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.messages[0]").value("Malformed or invalid request body"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void malformedJsonReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/expenses")
                        .with(user("1").roles("USER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\":"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.messages[0]").value("Malformed or invalid request body"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void accessingAnotherUsersExpenseReturnsForbiddenInsteadOfServerError() throws Exception {
        when(expenseService.getExpense(99L))
                .thenThrow(new org.springframework.security.access.AccessDeniedException("You cannot access this expense"));

        mockMvc.perform(get("/api/expenses/99")
                        .with(user("1").roles("USER")))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(403))
                .andExpect(jsonPath("$.messages[0]").value("You cannot access this expense"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void missingTokenReturnsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/expenses"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.messages[0]").value("Invalid or expired token"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void expiredTokenReturnsUnauthorizedWithoutInternalDetails() throws Exception {
        when(jwtService.extractUserId("expired-token"))
                .thenThrow(new ExpiredJwtException(null, null, "expired"));

        mockMvc.perform(get("/api/expenses")
                        .header("Authorization", "Bearer expired-token"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.messages[0]").value("Invalid or expired token"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void tamperedTokenReturnsUnauthorizedWithoutSignatureDetails() throws Exception {
        when(jwtService.extractUserId("tampered-token"))
                .thenThrow(new SignatureException("signature invalid"));

        mockMvc.perform(get("/api/expenses")
                        .header("Authorization", "Bearer tampered-token"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.messages[0]").value("Invalid or expired token"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void malformedTokenReturnsUnauthorizedWithoutInternalDetails() throws Exception {
        when(jwtService.extractUserId("malformed-token"))
                .thenThrow(new MalformedJwtException("jwt malformed"));

        mockMvc.perform(get("/api/expenses")
                        .header("Authorization", "Bearer malformed-token"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.messages[0]").value("Invalid or expired token"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void promotingAlreadyAdminReturnsConflict() throws Exception {
        doThrow(new UserAlreadyAdminException("User is already ADMIN"))
                .when(adminService).promoteUser(7L);

        mockMvc.perform(patch("/api/admin/users/7/promote")
                        .with(user("1").roles("ADMIN")))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.messages[0]").value("User is already ADMIN"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void analyticsTopCategoryReturnsStructuredNullResponseWhenEmpty() throws Exception {
        when(analyticsService.topCategory(2026, 3)).thenReturn(null);

        mockMvc.perform(get("/analytics/top-category")
                        .with(user("1").roles("USER"))
                        .param("year", "2026")
                        .param("month", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data").value(nullValue()))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void analyticsMonthlyTotalReturnsStructuredSuccessResponse() throws Exception {
        when(analyticsService.getMonthlyTotal(2026, 3)).thenReturn(2500.0);

        mockMvc.perform(get("/analytics/analytics/monthly-total")
                        .with(user("1").roles("USER"))
                        .param("year", "2026")
                        .param("month", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data").value(2500.0))
                .andExpect(jsonPath("$.timestamp").exists());
    }
}
