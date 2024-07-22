package com.esosa.f5pi_backend.data.models

import com.esosa.f5pi_backend.controllers.responses.UserResponse
import com.esosa.f5pi_backend.data.enums.Role
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import java.util.UUID

@Entity
data class User(
    val username: String,
    val password: String,

    val role: Role = Role.USER,

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL])
    val games: List<Game> = emptyList(),

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL])
    val players: List<Player> = emptyList(),

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL])
    val fields: List<Field> = emptyList(),

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL])
    val seasons: List<Season> = emptyList(),

    @Id
    val id: UUID = UUID.randomUUID()
) {
    fun buildUserResponse(): UserResponse = UserResponse(id, username, role.name)
}