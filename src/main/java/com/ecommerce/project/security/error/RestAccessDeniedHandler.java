package com.ecommerce.project.security.error;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class RestAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException {

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.getWriter().write(buildJson(
                403, "Forbidden",
                "You do not have permission to access this resource",
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