package com.esosa.f5pi_backend.data.repositories

import com.esosa.f5pi_backend.data.models.Season
import com.esosa.f5pi_backend.data.models.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ISeasonRepository : JpaRepository<Season, UUID> {
    fun findByUser(pageRequest: PageRequest, user: User): Page<Season>
    fun existsByNameAndUser(name: String, user: User): Boolean
}