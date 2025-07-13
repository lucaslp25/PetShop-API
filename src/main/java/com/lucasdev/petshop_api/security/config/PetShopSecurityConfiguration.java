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

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class PetShopSecurityConfiguration {

    private final PetShopFilter filter;

    // Array for better organization of my public matchers
    private static final String[] PUBLIC_MATCHERS = {
            "/auth/login",
            "/auth/register",
            "/v3/api-docs/**",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/h2-console/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http.csrf(pet -> pet.disable())
                //for my app, it´s better keep the csrf disabled, STATELESS APP

                //config for my h2 can be seen
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()))

                .sessionManagement(petSesh -> petSesh.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //turning explicit than is a STATELESS program

                .authorizeHttpRequests(pet -> pet
                        //doing the business rules

                        .requestMatchers(PUBLIC_MATCHERS).permitAll()

                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/products").hasRole("EMPLOYEE").requestMatchers(HttpMethod.DELETE, "/products").hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.PUT, "/products").hasRole("EMPLOYEE")

                        .anyRequest().authenticated())

                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)

                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {

        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}