package com.esosa.f5pi_backend.services.implementations

import com.esosa.f5pi_backend.controllers.requests.UpdateUserRequest
import com.esosa.f5pi_backend.controllers.responses.UserResponse
import com.esosa.f5pi_backend.data.models.User
import com.esosa.f5pi_backend.data.repositories.IUserRepository
import com.esosa.f5pi_backend.services.interfaces.IUserService
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.*

@Service
class UserService(
    private val userRepository: IUserRepository
) : IUserService {

    override fun saveUser(user: User): User =
        userRepository.save(user)

    override fun ifExistsUsernameThrowException(username: String) {
        if (userRepository.existsByUsername(username))
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists")
    }

    override fun findUserByUsernameOrThrowException(username: String): User =
        userRepository.findByUsername(username)
            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "User with username $username does not exist")

    override fun findUserByIdOrThrowException(userId: UUID): User =
        userRepository.findById(userId)
            .orElseThrow{ ResponseStatusException(HttpStatus.BAD_REQUEST, "User with id $userId does not exist") }

    override fun updateUser(userId: UUID, updateUserRequest: UpdateUserRequest): UserResponse =
        findUserByIdOrThrowException(userId).let { user ->
            userRepository.save(
                user.apply {
                    fullName = updateUserRequest.fullName
                    email = updateUserRequest.email
                }).buildUserResponse()
        }

    override fun deleteUser(userId: UUID) {
        ifUserDoesNotExistThrowException(userId)
        userRepository.deleteById(userId)
    }

    private fun ifUserDoesNotExistThrowException(userId: UUID) {
        if (!userRepository.existsById(userId))
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "User with id $userId does not exist")
    }
}