package com.ftms.filter;

// JwtAuthFilter runs BEFORE every API request.
// It checks the Authorization header for a JWT token.
// If token is valid, it tells Spring Security who the user is.
// This is how protected routes work: if no valid token, request is rejected with 401 Unauthorized.

import com.ftms.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Get the Authorization header from the incoming request
        // Frontend sends: Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
        final String authHeader = request.getHeader("Authorization");

        // If no Authorization header or it does not start with "Bearer ", skip this filter
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extract the token part (everything after "Bearer ")
        final String jwt = authHeader.substring(7);

        try {
            String userEmail = jwtService.extractEmail(jwt);
            String userRole = jwtService.extractRole(jwt);

            // If we got a valid email and no authentication is set yet, set it
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                
                // Create an authentication token with the user's role as authority
                // ROLE_ prefix is required by Spring Security
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userEmail,
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_" + userRole))
                );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                
                // Store authentication in Spring Security context for this request
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        } catch (Exception e) {
            // Invalid token - security context stays empty, request will be rejected by Spring Security
        }

        filterChain.doFilter(request, response);
    }
}
