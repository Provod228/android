package com.example.myapplication.data.repositories

import com.example.myapplication.data.models.UserDto
import com.example.myapplication.domain.models.User
import com.example.myapplication.domain.repositories.getUsersRepository
import com.example.myapplication.domain.repositories.addUserRepository

class getUsersRepositoryImpl: getUsersRepository, addUserRepository {
    private val listUsers = mutableListOf(
        UserDto(1, "Harry", "", 13),
        UserDto(2, "Sirius", "", 33),
    )

    override suspend fun getUsers(): List<User> {
        return listUsers.map { dto ->
            User(
                name = dto.name,
                age = dto.age,
                id = dto.id
            )
        }
    }

    override suspend fun addUser(user: User) {
        val newId = if (listUsers.isEmpty()) 1 else listUsers.maxOf { it.id } + 1
        listUsers.add(UserDto(
            id = newId,
            name = user.name,
            surname = "",
            age = user.age
        ))
    }
}