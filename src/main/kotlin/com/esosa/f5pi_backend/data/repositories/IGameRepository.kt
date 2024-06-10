package com.esosa.f5pi_backend.data.repositories

import com.esosa.f5pi_backend.data.models.Game
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface IGameRepository: JpaRepository<Game, UUID>