package com.example.fintech.day7.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * Reads the {@code Authorization: Bearer <token>} header on every request.
 * A valid token populates the {@link SecurityContextHolder}; an invalid or
 * missing token lets the request continue unauthenticated — the authorization
 * rules in {@link SecurityConfig} then reject it with 401/403.
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtService jwtService;

    public JwtAuthFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header != null && header.startsWith(BEARER_PREFIX)) {
            try {
                Claims claims = jwtService.parseToken(header.substring(BEARER_PREFIX.length()));

                @SuppressWarnings("unchecked")
                List<String> roles = claims.get("roles", List.class);
                var authorities = (roles == null ? List.<String>of() : roles).stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                        .toList();

                var authentication = new UsernamePasswordAuthenticationToken(
                        claims.getSubject(), null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (JwtException | IllegalArgumentException e) {
                // Bad signature, expired, malformed… — continue unauthenticated.
            }
        }
        filterChain.doFilter(request, response);
    }
}
