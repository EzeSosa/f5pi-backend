package com.esosa.f5pi_backend.controllers.implementations

import com.esosa.f5pi_backend.controllers.interfaces.ISeasonController
import com.esosa.f5pi_backend.controllers.requests.CreateSeasonRequest
import com.esosa.f5pi_backend.controllers.requests.UpdateSeasonRequest
import com.esosa.f5pi_backend.controllers.responses.SeasonResponse
import com.esosa.f5pi_backend.services.interfaces.ISeasonService
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class SeasonController(private val seasonService: ISeasonService) : ISeasonController {
    override fun saveSeason(createSeasonRequest: CreateSeasonRequest): SeasonResponse =
        seasonService.saveSeason(createSeasonRequest)

    override fun updateSeason(seasonId: UUID, updateSeasonRequest: UpdateSeasonRequest): SeasonResponse =
        seasonService.updateSeason(seasonId, updateSeasonRequest)

    override fun deleteSeason(seasonId: UUID) =
        seasonService.deleteSeason(seasonId)
}