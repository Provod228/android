package com.example.myapplication.domain.usecases

import com.example.myapplication.domain.models.User
import com.example.myapplication.domain.repositories.UpdateUserRepository

class UpdateUserUseCase(private val updateUserRepository: UpdateUserRepository) {
    suspend operator fun invoke(user: User) {
        updateUserRepository.updateUser(user)
    }
} 