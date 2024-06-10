package com.esosa.f5pi_backend.data.repositories

import com.esosa.f5pi_backend.data.models.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface IUserRepository: JpaRepository<User, UUID> {
    fun findByUsername(username: String): User?
    fun existsByUsername(username: String): Boolean
}