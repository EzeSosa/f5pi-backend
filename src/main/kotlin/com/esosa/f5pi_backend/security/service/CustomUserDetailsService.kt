package com.esosa.f5pi_backend.security.service

import com.esosa.f5pi_backend.data.repositories.IUserRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

typealias AppUser = com.esosa.f5pi_backend.data.models.User

@Service("customUserDetailsService")
class CustomUserDetailsService(private val userRepository: IUserRepository) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        return userRepository.findByUsername(username)
            ?.mapToUserDetails()
            ?: throw UsernameNotFoundException("Username not found")
    }

    private fun AppUser.mapToUserDetails() =
        User.builder()
            .username(username)
            .password(password)
            .roles(role.name)
            .build()
}