package com.example.Notes.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Basic security configuration for local development.
 *
 * This configuration permits unauthenticated access to the application API
 * (endpoints under /api/**) so the frontend can call the backend during
 * development. It disables CSRF (API use) and leaves other endpoints
 * protected if you later tighten rules.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			// Disable CSRF protection completely for API endpoints (required for POST/PUT/DELETE from frontend)
			.csrf(csrf -> csrf.disable())
			// Disable CORS at security level since we handle CORS at controller level with @CrossOrigin
			.cors(cors -> cors.disable())
			.authorizeHttpRequests(auth -> auth
				// Allow unauthenticated access to all API endpoints for local dev
				.requestMatchers("/api/**").permitAll()
				// Allow access to actuator endpoints for health checks
				.requestMatchers("/actuator/**").permitAll()
				// Allow access to root path and health endpoint
				.requestMatchers("/", "/health").permitAll()
				// Keep everything else authenticated by default
				.anyRequest().authenticated()
			)
			// Disable form login for API-only backend
			.formLogin(form -> form.disable())
			// Disable HTTP Basic authentication for dev
			.httpBasic(httpBasic -> httpBasic.disable())
			// Make sessions stateless (no server-side session for API)
			.sessionManagement(session -> session.sessionCreationPolicy(
				org.springframework.security.config.http.SessionCreationPolicy.STATELESS
			));

		return http.build();
	}
}
