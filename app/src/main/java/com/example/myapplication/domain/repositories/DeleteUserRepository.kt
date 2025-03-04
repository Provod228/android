package com.example.myapplication.domain.repositories

interface DeleteUserRepository {
    suspend fun deleteUser(name: String, surname: String): Boolean
} 