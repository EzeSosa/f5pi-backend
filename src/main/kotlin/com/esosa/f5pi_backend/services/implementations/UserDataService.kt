package com.esosa.f5pi_backend.services.implementations

import com.esosa.f5pi_backend.controllers.responses.FieldResponse
import com.esosa.f5pi_backend.controllers.responses.GameResponse
import com.esosa.f5pi_backend.controllers.responses.PlayerResponse
import com.esosa.f5pi_backend.controllers.responses.SeasonResponse
import com.esosa.f5pi_backend.services.interfaces.IFieldService
import com.esosa.f5pi_backend.services.interfaces.IGameService
import com.esosa.f5pi_backend.services.interfaces.IPlayerService
import com.esosa.f5pi_backend.services.interfaces.ISeasonService
import com.esosa.f5pi_backend.services.interfaces.IUserDataService
import com.esosa.f5pi_backend.services.interfaces.IUserService
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.*

@Service
class UserDataService(
    private val userService: IUserService,
    private val playerService: IPlayerService,
    private val seasonService: ISeasonService,
    private val gameService: IGameService,
    private val fieldService: IFieldService,
) : IUserDataService {

    override fun getUserPlayers(
        userId: UUID,
        pageNumber: Int,
        pageSize: Int,
        sortAttribute: String,
        sortOrder: String
    ): Page<PlayerResponse> =
        playerService.getUserPlayers(
            userService.findUserByIdOrThrowException(userId),
            pageNumber,
            pageSize,
            sortAttribute,
            sortOrder
        )

    override fun getUserSeasons(
        userId: UUID,
        pageNumber: Int,
        pageSize: Int,
        sortAttribute: String,
        sortOrder: String
    ): Page<SeasonResponse> =
        seasonService.getUserSeasons(
            userService.findUserByIdOrThrowException(userId),
            pageNumber,
            pageSize,
            sortAttribute,
            sortOrder
        )

    override fun getUserGames(
        userId: UUID,
        dateFrom: LocalDate?,
        dateTo: LocalDate?,
        fieldId: UUID?,
        seasonId: UUID?,
        pageNumber: Int,
        pageSize: Int,
        sortAttribute: String,
        sortOrder: String
    ): Page<GameResponse> =
        userService.findUserByIdOrThrowException(userId).let { user ->
            val field = fieldId?.let { fieldService.findFieldByIdOrThrowException(it) }
            val season = seasonId?.let { seasonService.findSeasonByIdOrThrowException(it) }
            gameService.getGamesByUser(user, dateFrom, dateTo, field, season, pageNumber, pageSize, sortAttribute, sortOrder)
        }

    override fun getUserFields(
        userId: UUID,
        pageNumber: Int,
        pageSize: Int,
        sortAttribute: String,
        sortOrder: String
    ): Page<FieldResponse> =
        fieldService.getUserFields(
            userService.findUserByIdOrThrowException(userId),
            pageNumber,
            pageSize,
            sortAttribute,
            sortOrder
        )
}