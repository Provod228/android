package com.example.myapplication.domain.repositories

import com.example.myapplication.domain.models.User

interface UpdateUserRepository {
    suspend fun updateUser(user: User)
} 