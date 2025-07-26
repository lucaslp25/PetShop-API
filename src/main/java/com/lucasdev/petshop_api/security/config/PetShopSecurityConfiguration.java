package com.lucasdev.petshop_api.security.config;

import com.lucasdev.petshop_api.security.services.PetShopFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class PetShopSecurityConfiguration {

    private final PetShopFilter filter;

    // Best Practice: Standardize all public routes with the API prefix.
    private static final String[] PUBLIC_MATCHERS = {
            "/api/v1/auth/**", // Covers both /login and /register
            "/api/v1/webhooks/**",
            "/v3/api-docs/**",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/h2-console/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable()) // Correct for stateless, token-based APIs
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin())) // For H2 Console
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth

                        //keeping the DRY
                        .requestMatchers(PUBLIC_MATCHERS).permitAll()

                        .requestMatchers(HttpMethod.POST, "/api/v1/sales").hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.POST, "/api/v1/products").hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/products").hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/products").hasRole("EMPLOYEE")

                        // Any other request not specified above requires authentication.
                        .anyRequest().authenticated()
                )
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // For production, it's better to specify the exact frontend domain instead of "*".
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}