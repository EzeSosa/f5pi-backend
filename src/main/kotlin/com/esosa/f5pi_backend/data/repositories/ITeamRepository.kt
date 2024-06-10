package com.esosa.f5pi_backend.data.repositories

import com.esosa.f5pi_backend.data.models.Team
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ITeamRepository: JpaRepository<Team, UUID>