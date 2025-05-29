package com.esosa.f5pi_backend.services.interfaces

import com.esosa.f5pi_backend.controllers.requests.UpdateUserRequest
import com.esosa.f5pi_backend.controllers.responses.UserResponse
import com.esosa.f5pi_backend.data.models.User
import java.util.*

interface IUserService {
    fun saveUser(user: User): User
    fun ifExistsUsernameThrowException(username: String)
    fun findUserByUsernameOrThrowException(username: String): User
    fun findUserByIdOrThrowException(userId: UUID): User
    fun updateUser(userId: UUID, updateUserRequest: UpdateUserRequest): UserResponse
    fun deleteUser(userId: UUID)
}