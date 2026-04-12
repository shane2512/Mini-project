package com.ftms.config;

// SecurityConfig defines which routes are public and which need authentication.
// It also configures CORS (allows your frontend on Netlify to call your backend on Render).
// BCryptPasswordEncoder is defined here - used to hash passwords.

import com.ftms.filter.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    @Value("${cors.allowed.origins}")
    private String allowedOrigins;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF because we use JWT tokens, not browser sessions
                .csrf(csrf -> csrf.disable())

                // Enable CORS with our configuration
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // Configure which routes need authentication
                .authorizeHttpRequests(auth -> auth
                        // These routes are PUBLIC - anyone can access
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/forex/**").permitAll() // public rate display and live preview conversion

                        // These routes need ADMIN role
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // These routes need CENTRAL_BANK role
                        .requestMatchers("/api/central-bank/**").hasRole("CENTRAL_BANK")

                        // These routes need COMMERCIAL_BANK role
                        .requestMatchers("/api/bank/**").hasRole("COMMERCIAL_BANK")

                        // Everything else needs any authenticated user
                        .anyRequest().authenticated())

                // Use stateless session - no server-side session storage, JWT handles
                // everything
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Add our JWT filter before Spring's default username/password filter
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCrypt is a secure hashing algorithm for passwords
        // It automatically adds salt (random data) to prevent rainbow table attacks
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Allow requests from your frontend URL (Netlify or localhost for development)
        List<String> origins = Arrays.stream(allowedOrigins.split(","))
                .map(String::trim)
                .toList();
        configuration.setAllowedOrigins(origins);

        // Allow these HTTP methods including OPTIONS for preflight
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));

        // Allow all headers including Authorization (for JWT)
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setAllowCredentials(false);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
