package com.esosa.f5pi_backend.controllers.interfaces

import com.esosa.f5pi_backend.controllers.requests.GenerateTeamsRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus

@RequestMapping("/api/v1/ai")
@Tag(
    name = "AI Controller",
    description = "Allows registered users to use AI to generate teams based on players statistics."
)
interface IAIController {
    @PostMapping("/generate-teams")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Generates a team based on player statistics.")
    fun generateTeams(@RequestBody @Valid generateTeamsRequest: GenerateTeamsRequest): String
}