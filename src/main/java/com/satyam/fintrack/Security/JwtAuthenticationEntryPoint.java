package com.satyam.fintrack.Security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.satyam.fintrack.exceptions.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;
    private static final String INVALID_TOKEN_MESSAGE = "Invalid or expired token";

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException)
            throws IOException, ServletException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(objectMapper.writeValueAsString(
                new ErrorResponse(
                        HttpServletResponse.SC_UNAUTHORIZED,
                        List.of(INVALID_TOKEN_MESSAGE),
                        LocalDateTime.now()
                )
        ));
    }
}
