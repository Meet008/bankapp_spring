package com.bankapp.dashboard.config;

import com.bankapp.dashboard.security.JwtAuthFilter;
import com.bankapp.dashboard.security.RestAccessDeniedHandler;
import com.bankapp.dashboard.security.RestAuthEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final RestAuthEntryPoint authEntryPoint;
    private final RestAccessDeniedHandler accessDeniedHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> {
                })
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(requests -> requests
                        // PUBLIC endpoints (no auth)
                        .requestMatchers(
                                "/api/auth/login",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"

                        ).permitAll()

                        // CUSTOMER + ADMIN: profile endpoints
                        .requestMatchers("/api/users/me", "/api/users/roles").authenticated()

                        // ADMIN‑only endpoints
                        .requestMatchers("/api/users/**").hasRole("ADMIN")
                        .requestMatchers("/api/accounts").hasRole("ADMIN")      // get all accounts
                        .requestMatchers("/api/transactions").hasRole("ADMIN")  // get all transactions
                        .requestMatchers("/api/payments").hasRole("ADMIN")      // get all payments

                        // everything else: any logged‑in user (ADMIN or CUSTOMER)
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(authEntryPoint)   // 401 → ErrorResponse
                        .accessDeniedHandler(accessDeniedHandler)   // 403 → ErrorResponse
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
