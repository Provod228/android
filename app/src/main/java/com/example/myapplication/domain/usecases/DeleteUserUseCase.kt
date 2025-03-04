package com.example.myapplication.domain.usecases

import com.example.myapplication.domain.repositories.DeleteUserRepository

class DeleteUserUseCase(private val deleteUserRepository: DeleteUserRepository) {
    suspend operator fun invoke(name: String, surname: String): Boolean {
        return deleteUserRepository.deleteUser(name, surname)
    }
} 