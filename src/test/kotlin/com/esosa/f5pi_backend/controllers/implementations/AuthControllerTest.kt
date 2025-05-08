package com.esosa.f5pi_backend.controllers.implementations

import com.esosa.f5pi_backend.controllers.base.BaseIntegrationTest
import com.esosa.f5pi_backend.controllers.requests.LoginRequest
import com.esosa.f5pi_backend.controllers.requests.RegisterRequest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class AuthControllerTest : BaseIntegrationTest() {

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @Test
    @Sql("/scripts/users/insert_users.sql")
    fun `test register`() {
        val userCountBefore = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM APP_USER",
            Int::class.java
        )

        RegisterRequest(
            "DELETE FROM USER WHERE 1 = 1",
            "DELETE FROM GAME WHERE 1 = 2",
            "DELETE FROM FIELD WHERE 1 = 1",
            "test@gmail.com"
        ).also {
            mockMvc.perform(
                post("/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(toJson(it))
            ).andExpect(status().isCreated)
        }.also {
            mockMvc.perform(
                post("/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(toJson(LoginRequest(it.username, it.password)))
            ).andExpect(status().isOk)
        }


        val userCountAfter = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM APP_USER",
            Int::class.java
        )

        assertEquals(userCountBefore?.plus(1), userCountAfter)
    }
}