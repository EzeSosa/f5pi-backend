package com.esosa.f5pi_backend.controllers.security

import com.esosa.f5pi_backend.security.jwt.JWTService
import com.esosa.f5pi_backend.security.service.CustomUserDetailsService
import com.esosa.f5pi_backend.security.utils.Constants.Companion.CLAIMS_JWT_EXCEPTION_MESSAGE
import com.esosa.f5pi_backend.security.utils.Constants.Companion.MISSING_HEADER_EXCEPTION_MESSAGE
import com.esosa.f5pi_backend.security.utils.Constants.Companion.USERNAME_NOT_FOUND_MESSAGE
import io.jsonwebtoken.MalformedJwtException
import org.hamcrest.Matchers.containsString
import org.junit.jupiter.api.Test
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class JWTFilterTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var jwtService: JWTService

    @MockBean(name = "customUserDetailsService")
    private lateinit var customUserDetailsService: CustomUserDetailsService

    companion object {
        private const val TEST_TOKEN = "jwt.auth.token"
        private const val TEST_USERNAME = "jwt.username"
        private const val TEST_ENDPOINT = "/api/v1/demo/ping"
    }

    @Test
    fun `should allow access with valid JWT`() {
        setupValidJwtFlow()
        mockMvc.perform(
            get(TEST_ENDPOINT).withBearerToken(TEST_TOKEN)
        ).andExpect(status().isOk)
    }

    @Test
    fun `should deny access with no auth header`() {
        mockMvc.perform(get(TEST_ENDPOINT))
            .andExpect(status().isUnauthorized)
            .andExpect(jsonPath("$.message").value(containsString(MISSING_HEADER_EXCEPTION_MESSAGE.first)))
    }

    @Test
    fun `should deny access with malformed JWT`() {
        setupMalformedJwtFlow()
        mockMvc.perform(
            get(TEST_ENDPOINT).withBearerToken(TEST_TOKEN)
        ).andExpect(status().isForbidden)
            .andExpect(jsonPath("$.message").value(containsString(CLAIMS_JWT_EXCEPTION_MESSAGE.first)))
    }

    @Test
    fun `should deny access with non existent user`() {
        setupNonExistentUserFlow()
        mockMvc.perform(
            get(TEST_ENDPOINT).withBearerToken(TEST_TOKEN)
        ).andExpect(status().isForbidden)
            .andExpect(jsonPath("$.message").value(containsString(USERNAME_NOT_FOUND_MESSAGE.first)))
    }

    private fun setupValidJwtFlow() {
        mock<UserDetails>().also {
            whenever(jwtService.extractUsernameFromToken(eq(TEST_TOKEN))).thenReturn(TEST_USERNAME)
            whenever(customUserDetailsService.loadUserByUsername(TEST_USERNAME)).thenReturn(it)
            whenever(jwtService.isTokenValid(TEST_TOKEN, TEST_USERNAME)).thenReturn(true)
        }
    }

    private fun setupMalformedJwtFlow() {
        whenever(jwtService.extractUsernameFromToken(TEST_TOKEN))
            .thenThrow(MalformedJwtException("Malformed JWT"))
    }

    private fun setupNonExistentUserFlow() {
        whenever(jwtService.extractUsernameFromToken(eq(TEST_TOKEN))).thenReturn(TEST_USERNAME)
        whenever(customUserDetailsService.loadUserByUsername(TEST_USERNAME))
            .thenThrow(UsernameNotFoundException("Username not found"))
    }

    private fun MockHttpServletRequestBuilder.withBearerToken(token: String) =
        header(AUTHORIZATION, "Bearer $token")
}