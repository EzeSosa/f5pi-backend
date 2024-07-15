package com.esosa.f5pi_backend.data.repositories

import com.esosa.f5pi_backend.data.models.Field
import com.esosa.f5pi_backend.data.models.Game
import com.esosa.f5pi_backend.data.models.Season
import com.esosa.f5pi_backend.data.models.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDate
import java.util.UUID

interface IGameRepository: JpaRepository<Game, UUID> {
    @Query( "SELECT g from Game g WHERE " +
            "g.user = ?1 " +
            "AND (?2 is null or g.date >= ?2) " +
            "AND (?3 is null or g.date <= ?3) " +
            "AND (?4 is null or g.official = ?4) " +
            "AND (?5 is null or g.field = ?5) " +
            "AND (?6 is null or g.season = ?6)"
    )
    fun findByUser(
        user: User,
        dateFrom: LocalDate?,
        dateTo: LocalDate?,
        official: Boolean?,
        field: Field?,
        season: Season?
    ): List<Game>
}