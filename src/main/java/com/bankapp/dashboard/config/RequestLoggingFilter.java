package com.bankapp.dashboard.config;

import com.bankapp.dashboard.security.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class RequestLoggingFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        long start = System.currentTimeMillis();
        String requestId = UUID.randomUUID().toString();

        // Put requestId in MDC so it's on every log line for this request
        MDC.put("requestId", requestId);

        // Try to extract user email from Authorization header (JWT)
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String userEmail = "anonymous";

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                userEmail = jwtService.extractUsername(token);
            } catch (Exception ex) {
                // invalid token, keep anonymous
            }
        }
        MDC.put("userEmail", userEmail);

        String method = request.getMethod();
        String path = request.getRequestURI();
        String query = request.getQueryString();

        log.info("Incoming request method={} path={} query={} user={}",
                method, path, query, userEmail);

        try {
            filterChain.doFilter(request, response);
        } finally {
            long duration = System.currentTimeMillis() - start;
            int status = response.getStatus();

            log.info("Completed request method={} path={} status={} durationMs={} at={} user={}",
                    method, path, status, duration, Instant.now(), userEmail);

            // Clean up MDC
            MDC.remove("requestId");
            MDC.remove("userEmail");
        }
    }
}
