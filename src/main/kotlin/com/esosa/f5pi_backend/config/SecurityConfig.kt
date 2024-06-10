package com.esosa.f5pi_backend.config

import com.esosa.f5pi_backend.security.filter.JWTAuthenticationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val authProvider: AuthenticationProvider,
) {

    @Bean
    fun securityFilterChain(
        http: HttpSecurity,
        jwtAuthenticationFilter: JWTAuthenticationFilter
    ) : DefaultSecurityFilterChain =
        http
            .csrf { csrf -> csrf.disable() }
            .authorizeHttpRequests { httpRequests ->
                httpRequests
                    .requestMatchers("/auth/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "api/v1/**").permitAll()
                    .anyRequest().authenticated()
            }
            .sessionManagement { management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .httpBasic { httpBasic -> httpBasic.disable() }
            .authenticationProvider(authProvider)
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .build()
}