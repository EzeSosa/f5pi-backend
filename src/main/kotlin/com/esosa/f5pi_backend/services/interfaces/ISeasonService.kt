package com.esosa.f5pi_backend.services.interfaces

import com.esosa.f5pi_backend.controllers.requests.CreateSeasonRequest
import com.esosa.f5pi_backend.controllers.requests.UpdateSeasonRequest
import com.esosa.f5pi_backend.controllers.responses.SeasonResponse
import com.esosa.f5pi_backend.data.models.Season
import java.util.UUID

interface ISeasonService {
    fun saveSeason(createSeasonRequest: CreateSeasonRequest): SeasonResponse
    fun updateSeason(seasonId: UUID, updateSeasonRequest: UpdateSeasonRequest): SeasonResponse
    fun deleteSeason(seasonId: UUID)
    fun findSeasonByIdOrThrowException(seasonId: UUID): Season
}