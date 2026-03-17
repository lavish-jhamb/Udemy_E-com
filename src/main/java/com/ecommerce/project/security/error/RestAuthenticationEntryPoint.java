package com.ecommerce.project.security.error;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException) throws IOException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write(buildJson(
                401, "Unauthorized",
                "Full authentication is required to access this resource",
                request.getRequestURI()));
    }

    private String buildJson(int status, String error, String message, String path) {
        return "{\"status\":" + status
                + ",\"error\":\"" + error + "\""
                + ",\"message\":\"" + message + "\""
                + ",\"path\":\"" + path + "\""
                + ",\"timestamp\":\"" + LocalDateTime.now() + "\"}"
                ;
    }
}