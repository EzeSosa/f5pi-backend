package com.esosa.f5pi_backend.controllers.implementations

import com.esosa.f5pi_backend.controllers.base.BaseIntegrationTest
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.http.MediaType
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.util.*
import java.util.stream.Stream

class UserControllerTest : BaseIntegrationTest() {
    companion object UserTestData {
        val USER_UUID_WITH = UUID.fromString("58fae25b-ea38-4e7b-ab2d-9f555a67836b")
        val USER_UUID_WITHOUT = UUID.fromString("68fae25b-ea38-4e7b-ab2d-9f555a67836e")

        @JvmStatic
        private fun endpointProviderForUserDoesNotExist(): Stream<Arguments> = Stream.of(
            Arguments.of("/api/v1/users/{userId}/fields", UUID.randomUUID()),
            Arguments.of("/api/v1/users/{userId}/players", UUID.randomUUID()),
            Arguments.of("/api/v1/users/{userId}/games", UUID.randomUUID())
        )

        @JvmStatic
        private fun endpointProviderForExistentUser(): Stream<Arguments> = Stream.of(
            Arguments.of("/api/v1/users/{userId}/fields", USER_UUID_WITH, 1, "createdAt"),
            Arguments.of("/api/v1/users/{userId}/fields", USER_UUID_WITHOUT, 0, "createdAt"),
            Arguments.of("/api/v1/users/{userId}/players", USER_UUID_WITH, 1, "createdAt"),
            Arguments.of("/api/v1/users/{userId}/players", USER_UUID_WITHOUT, 0, "createdAt"),
            Arguments.of("/api/v1/users/{userId}/games", USER_UUID_WITH, 1, "date"),
            Arguments.of("/api/v1/users/{userId}/games", USER_UUID_WITHOUT, 0, "date"),

            )
    }

    @ParameterizedTest
    @MethodSource("endpointProviderForUserDoesNotExist")
    fun `should return bad request when user does not exist and we try to get fields or players or games`(
        endpoint: String,
        userId: UUID
    ) {
        mockMvc.perform(
            get(endpoint, userId.toString())
                .param("pageNumber", "0")
                .param("pageSize", "2")
                .param("sortAttribute", "createdAt")
                .param("sortOrder", "desc")
        )
            .andExpect(status().isBadRequest)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message").value("User with id $userId does not exist"))
    }

    @ParameterizedTest
    @MethodSource("endpointProviderForExistentUser")
    @Sql(
        "/scripts/fields/insert_fields.sql",
        "/scripts/seasons/insert_seasons.sql",
        "/scripts/players/insert_players.sql",
        "/scripts/games/insert_games.sql"
    )
    fun `should return ok when user has fields or players or games`(
        endpoint: String,
        userId: UUID,
        expectedValue: Int,
        sortAttributeValue: String
    ) {
        mockMvc.perform(
            get(endpoint, userId.toString())
                .param("pageNumber", "0")
                .param("pageSize", "2")
                .param("sortAttribute", sortAttributeValue)
                .param("sortOrder", "desc")
        ).andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.content.length()").value(expectedValue))
    }
}