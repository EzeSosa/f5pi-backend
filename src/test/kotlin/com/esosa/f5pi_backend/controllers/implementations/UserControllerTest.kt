package com.esosa.f5pi_backend.controllers.implementations

import com.esosa.f5pi_backend.controllers.base.BaseIntegrationTest
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.http.MediaType
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.util.*

class UserControllerTest : BaseIntegrationTest() {
    companion object UserTestData {
        val USER_UUID_WITH = UUID.fromString("58fae25b-ea38-4e7b-ab2d-9f555a67836b")
        val USER_UUID_WITHOUT = UUID.fromString("68fae25b-ea38-4e7b-ab2d-9f555a67836e")
    }

    @Test
    fun `should return bad request when user does not exists and we try to get fields`() {
        val userId = UUID.randomUUID()
        mockMvc.perform(
            get("/api/v1/users/{userId}/fields", userId.toString())
                .param("pageNumber", "0")
                .param("pageSize", "2")
                .param("sortAttribute", "createdAt")
                .param("sortOrder", "desc")
        )
            .andExpect(status().isBadRequest)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message").value("User with id $userId does not exist"))
    }


    @Test
    @Sql("/scripts/fields/insert_fields.sql")
    fun `should return ok when user has fields`() {

        mockMvc.perform(
            get("/api/v1/users/{userId}/fields", USER_UUID_WITH.toString())
                .param("pageNumber", "0")
                .param("pageSize", "2")
                .param("sortAttribute", "createdAt")
                .param("sortOrder", "desc")
        ).andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.content.length()").value(1))
    }

    @Test
    @Sql("/scripts/fields/insert_fields.sql")
    fun `should return ok when user does not have fields`() {

        mockMvc.perform(
            get("/api/v1/users/{userId}/fields", USER_UUID_WITHOUT.toString())
                .param("pageNumber", "0")
                .param("pageSize", "2")
                .param("sortAttribute", "createdAt")
                .param("sortOrder", "desc")
        ).andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.content.length()").value(0))
    }

    @Test
    fun `should return bad request when user does not exists and we try to get players`() {
        val userId = UUID.randomUUID()
        mockMvc.perform(
            get("/api/v1/users/{userId}/players", userId.toString())
                .param("pageNumber", "0")
                .param("pageSize", "2")
                .param("sortAttribute", "createdAt")
                .param("sortOrder", "desc")
        )
            .andExpect(status().isBadRequest)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message").value("User with id $userId does not exist"))
    }

    @Test
    @Sql("/scripts/players/insert_players.sql")
    fun `should return ok when user has players`() {
        mockMvc.perform(
            get("/api/v1/users/{userId}/players", USER_UUID_WITH.toString())
                .param("pageNumber", "0")
                .param("pageSize", "2")
                .param("sortAttribute", "createdAt")
                .param("sortOrder", "desc")
        ).andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.content.length()").value(1))

    }

    @Test
    @Sql("/scripts/players/insert_players.sql")
    fun `should return ok when user does not have players`() {
        mockMvc.perform(
            get("/api/v1/users/{userId}/players", USER_UUID_WITHOUT.toString())
                .param("pageNumber", "0")
                .param("pageSize", "2")
                .param("sortAttribute", "createdAt")
                .param("sortOrder", "desc")
        ).andExpect { content().contentType(MediaType.APPLICATION_JSON) }
            .andExpect { jsonPath("$.content.length()").value(0) }
    }

    @Test
    fun `should return bad request when user does not exists and we try to get games`() {
        val userId = UUID.randomUUID()
        mockMvc.perform(
            get("/api/v1/users/{userId}/fields", userId.toString())
                .param("pageNumber", "0")
                .param("pageSize", "2")
                .param("sortAttribute", "createdAt")
                .param("sortOrder", "desc")
        )
            .andExpect(status().isBadRequest)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message").value("User with id $userId does not exist"))
    }

    @Test
    @Sql("/scripts/games/insert_games.sql")
    fun `should return ok when user has games`() {

        mockMvc.perform(
            get("/api/v1/users/{userId}/games", USER_UUID_WITH.toString())
                .param("pageNumber", "0")
                .param("pageSize", "2")
                .param("sortAttribute", "createdAt")
                .param("sortOrder", "desc")
        ).andExpect { content().contentType(MediaType.APPLICATION_JSON) }
            .andExpect { jsonPath("$.content.length()").value(1) }
    }

    @Test
    @Sql("/scripts/games/insert_games.sql")
    fun `should return ok when user does not have games`() {
        mockMvc.perform(
            get("/api/v1/users/{userId}/games", USER_UUID_WITH.toString())
                .param("pageNumber", "0")
                .param("pageSize", "2")
                .param("sortAttribute", "createdAt")
                .param("sortOrder", "desc")
        ).andExpect { content().contentType(MediaType.APPLICATION_JSON) }
            .andExpect { jsonPath("$.content.length()").value(0) }
    }
}