package com.esosa.f5pi_backend.controllers.implementations

import com.esosa.f5pi_backend.controllers.base.BaseIntegrationTest
import com.esosa.f5pi_backend.controllers.requests.CreateFieldRequest
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.UUID

class FieldControllerTest : BaseIntegrationTest() {

    @Test
    fun `should return bad request when user does not exist`() {
        val userId = UUID.randomUUID()
        val fieldRequest = CreateFieldRequest("Test Field", userId)
        val jsonRequest = toJson(fieldRequest)

        mockMvc.perform(
            post("/api/v1/fields")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest)
        ).andExpect(status().isBadRequest)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message").value("User with id $userId does not exist"))
    }

    @Test
    @Sql("/scripts/fields/insert_fields.sql")
    fun `should return bad request when field already exist`() {
        val userId = UUID.fromString("58fae25b-ea38-4e7b-ab2d-9f555a67836b")
        val fieldRequest = CreateFieldRequest("Field Test", userId)
        val jsonRequest = toJson(fieldRequest)

        mockMvc.perform(
            post("/api/v1/fields")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest)
        ).andExpect(status().isBadRequest)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message").value("Field name ${fieldRequest.name} already exists for the user"))
    }

    @Test
    @Sql("/scripts/fields/insert_fields.sql")
    fun `should return created when field is created`() {
        val userId = UUID.fromString("58fae25b-ea38-4e7b-ab2d-9f555a67836b")
        val fieldRequest = CreateFieldRequest("Field Test 2", userId)
        val jsonRequest = toJson(fieldRequest)

        mockMvc.perform(
            post("/api/v1/fields")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest)
        ).andExpect(status().isCreated)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.fieldName").value(fieldRequest.name))
    }
}