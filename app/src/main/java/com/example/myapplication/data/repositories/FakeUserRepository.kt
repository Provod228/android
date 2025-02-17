package com.example.myapplication.data.repositories

import com.example.myapplication.data.models.UserDto
import com.example.myapplication.domain.models.User
import com.example.myapplication.domain.repositories.getUsersRepository
import com.example.myapplication.domain.repositories.addUserRepository
import com.example.myapplication.domain.repositories.UpdateUserRepository

class FakeUserRepository : getUsersRepository, addUserRepository, UpdateUserRepository {
    private val listUsers = mutableListOf<UserDto>()

    override suspend fun getUsers(): List<User> {
        return listUsers.map { User(it.name, it.age, it.id) }
    }

    override suspend fun addUser(user: User) {
        val newId = if (listUsers.isEmpty()) 1 else listUsers.maxOf { it.id } + 1
        listUsers.add(UserDto(newId, user.name, "", user.age))
    }

    override suspend fun updateUser(user: User) {
        val index = listUsers.indexOfFirst { it.id == user.id }
        if (index != -1) {
            listUsers[index] = UserDto(user.id, user.name, "", user.age)
        }
    }
} 