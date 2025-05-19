package com.esosa.f5pi_backend.config

import com.esosa.f5pi_backend.security.entrypoint.JWTAuthEntryPoint
import com.esosa.f5pi_backend.security.filter.JWTAuthenticationFilter
import com.esosa.f5pi_backend.utils.SWAGGER_URLS
import com.esosa.f5pi_backend.utils.WHITE_LIST_URLS
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val authProvider: AuthenticationProvider,
    private val entryPoint: JWTAuthEntryPoint,
) {

    @Bean
    fun securityFilterChain(
        http: HttpSecurity,
        jwtAuthenticationFilter: JWTAuthenticationFilter
    ) : SecurityFilterChain =
        http
            .csrf { csrf -> csrf.disable() }
            .authorizeHttpRequests { httpRequests ->
                httpRequests
                    .requestMatchers(*WHITE_LIST_URLS, *SWAGGER_URLS).permitAll()
                    .requestMatchers(HttpMethod.GET, "api/v1/**").permitAll()
                    .anyRequest().authenticated()
            }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .httpBasic { it.disable() }
            .exceptionHandling { it.authenticationEntryPoint(entryPoint) }
            .authenticationProvider(authProvider)
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .build()
}