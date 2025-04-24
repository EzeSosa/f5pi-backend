package com.esosa.f5pi_backend.controllers.implementations

import com.esosa.f5pi_backend.controllers.base.BaseIntegrationTest
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.http.MediaType
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.util.*

class UserControllerTest : BaseIntegrationTest()  {
 @Test
 fun `should return bad request when user does not exists`() {
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
   val userId = UUID.fromString("58fae25b-ea38-4e7b-ab2d-9f555a67836b")

   mockMvc.perform(
    get("/api/v1/users/{userId}/fields", userId.toString())
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
   val userId = UUID.fromString("68fae25b-ea38-4e7b-ab2d-9f555a67836e")

   mockMvc.perform(
    get("/api/v1/users/{userId}/fields", userId.toString())
        .param("pageNumber", "0")
        .param("pageSize", "2")
        .param("sortAttribute", "createdAt")
        .param("sortOrder", "desc")
   ).andExpect(status().isOk)
     .andExpect(content().contentType(MediaType.APPLICATION_JSON))
       .andExpect(jsonPath("$.content.length()").value(0))
 }

}