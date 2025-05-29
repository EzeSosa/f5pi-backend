package com.esosa.f5pi_backend.services.interfaces

import com.esosa.f5pi_backend.controllers.responses.FieldResponse
import com.esosa.f5pi_backend.controllers.responses.GameResponse
import com.esosa.f5pi_backend.controllers.responses.PlayerResponse
import com.esosa.f5pi_backend.controllers.responses.SeasonResponse
import org.springframework.data.domain.Page
import java.time.LocalDate
import java.util.*

interface IUserDataService {
    fun getUserPlayers(
        userId: UUID,
        pageNumber: Int,
        pageSize: Int,
        sortAttribute: String,
        sortOrder: String
    ): Page<PlayerResponse>

    fun getUserGames(
        userId: UUID,
        dateFrom: LocalDate?,
        dateTo: LocalDate?,
        fieldId: UUID?,
        seasonId: UUID?,
        pageNumber: Int,
        pageSize: Int,
        sortAttribute: String,
        sortOrder: String
    ): Page<GameResponse>

    fun getUserFields(
        userId: UUID,
        pageNumber: Int,
        pageSize: Int,
        sortAttribute: String,
        sortOrder: String
    ): Page<FieldResponse>

    fun getUserSeasons(
        userId: UUID,
        pageNumber: Int,
        pageSize: Int,
        sortAttribute: String,
        sortOrder: String
    ): Page<SeasonResponse>
}