package com.esosa.f5pi_backend.config

import com.esosa.f5pi_backend.data.repositories.IUserRepository
import com.esosa.f5pi_backend.security.jwt.JWTProperties
import com.esosa.f5pi_backend.security.service.CustomUserDetailsService
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
@EnableConfigurationProperties(JWTProperties::class)
class AuthConfig {

    @Bean
    fun authenticationProvider(userRepository: IUserRepository): AuthenticationProvider =
        DaoAuthenticationProvider().also { dao ->
            dao.setUserDetailsService(userDetailsService(userRepository))
            dao.setPasswordEncoder(passwordEncoder())
        }

    @Bean
    fun authenticationManager(authConfig: AuthenticationConfiguration): AuthenticationManager =
        authConfig.authenticationManager

    @Bean
    fun userDetailsService(userRepository: IUserRepository): CustomUserDetailsService =
        CustomUserDetailsService(userRepository)

    @Bean
    fun passwordEncoder(): PasswordEncoder =
        BCryptPasswordEncoder()
}