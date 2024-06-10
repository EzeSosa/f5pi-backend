package com.esosa.f5pi_backend.data.models

import com.esosa.f5pi_backend.data.enums.Role
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "_user")
data class User(
    val username: String,
    val password: String,

    val role: Role = Role.USER,

    @OneToMany(mappedBy = "user")
    val games: List<Game> = emptyList(),

    @OneToMany(mappedBy = "user")
    val players: List<Player> = emptyList(),

    @Id
    val id: UUID = UUID.randomUUID()
)