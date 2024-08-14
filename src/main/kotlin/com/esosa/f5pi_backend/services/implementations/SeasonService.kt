package com.esosa.f5pi_backend.services.implementations

import com.esosa.f5pi_backend.controllers.requests.CreateSeasonRequest
import com.esosa.f5pi_backend.controllers.requests.UpdateSeasonRequest
import com.esosa.f5pi_backend.controllers.responses.SeasonResponse
import com.esosa.f5pi_backend.data.models.Season
import com.esosa.f5pi_backend.data.models.User
import com.esosa.f5pi_backend.data.repositories.ISeasonRepository
import com.esosa.f5pi_backend.services.interfaces.ISeasonService
import com.esosa.f5pi_backend.services.interfaces.IUserService
import com.esosa.f5pi_backend.utils.PageMapper
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.UUID

@Service
class SeasonService(
    private val seasonRepository: ISeasonRepository,
    private val userService: IUserService
) : ISeasonService {

    override fun saveSeason(createSeasonRequest: CreateSeasonRequest): SeasonResponse =
        with(createSeasonRequest) {
            val user = userService.findUserByIdOrThrowException(userId)
            seasonRepository.save(buildSeason(user))
                .buildSeasonResponse()
        }

    override fun updateSeason(seasonId: UUID, updateSeasonRequest: UpdateSeasonRequest): SeasonResponse =
        findSeasonByIdOrThrowException(seasonId).let { season ->
            seasonRepository.save(season.apply {
                name = updateSeasonRequest.name
                initialDate = updateSeasonRequest.initialDate
                finalDate = updateSeasonRequest.finalDate
            }).buildSeasonResponse()
        }

    override fun deleteSeason(seasonId: UUID) {
        ifSeasonDoesNotExistThrowException(seasonId)
        seasonRepository.deleteById(seasonId)
    }

    override fun findSeasonByIdOrThrowException(seasonId: UUID): Season =
        seasonRepository.findById(seasonId)
            .orElseThrow { ResponseStatusException(HttpStatus.BAD_REQUEST, "Season with id $seasonId does not exist") }

    override fun getUserSeasons(user: User, pageNumber: Int, pageSize: Int): Page<SeasonResponse> =
        seasonRepository.findByUser( PageMapper.buildPageRequest(pageNumber, pageSize), user )
            .map(Season::buildSeasonResponse)

    private fun ifSeasonDoesNotExistThrowException(seasonId: UUID) {
        if (!seasonRepository.existsById(seasonId))
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Season with id $seasonId does not exist")
    }

    private fun CreateSeasonRequest.buildSeason(user: User) = Season(name, initialDate, finalDate, user)
}