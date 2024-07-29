package com.esosa.f5pi_backend.controllers.interfaces

import com.esosa.f5pi_backend.controllers.requests.CreateSeasonRequest
import com.esosa.f5pi_backend.controllers.requests.UpdateSeasonRequest
import com.esosa.f5pi_backend.controllers.responses.SeasonResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import java.util.UUID

@RequestMapping("/api/v1/seasons")
@Tag(
    name = "Seasons",
    description = "Allows registered users to register, update and delete their seasons."
)
interface ISeasonController {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registers a new season for a registered user")
    fun saveSeason(@RequestBody @Valid createSeasonRequest: CreateSeasonRequest): SeasonResponse

    @PatchMapping("/{seasonId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Updates an existent season for a registered user")
    fun updateSeason(@PathVariable seasonId: UUID, @RequestBody @Valid updateSeasonRequest: UpdateSeasonRequest): SeasonResponse

    @DeleteMapping("/{seasonId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deletes an existent season for a registered user")
    fun deleteSeason(@PathVariable seasonId: UUID)
}