package com.esosa.f5pi_backend.security.service

import com.esosa.f5pi_backend.data.repositories.IUserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.security.core.userdetails.User

typealias AppUser = com.esosa.f5pi_backend.data.models.User

@Service
class CustomUserDetailsService(private val userRepository: IUserRepository) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails =
        userRepository.findByUsername(username)
            ?.mapToUserDetails()
            ?: throw UsernameNotFoundException("Username not found")

    private fun AppUser.mapToUserDetails() =
        User.builder()
            .username(username)
            .password(password)
            .roles(role.name)
            .build()
}