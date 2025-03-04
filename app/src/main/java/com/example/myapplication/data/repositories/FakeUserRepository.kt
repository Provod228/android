package com.example.myapplication.data.repositories

import com.example.myapplication.data.models.UserDto
import com.example.myapplication.domain.models.User
import com.example.myapplication.domain.repositories.getUsersRepository
import com.example.myapplication.domain.repositories.addUserRepository
import com.example.myapplication.domain.repositories.UpdateUserRepository
import com.example.myapplication.domain.repositories.DeleteUserRepository

class FakeUserRepository : 
    getUsersRepository, 
    addUserRepository, 
    UpdateUserRepository,
    DeleteUserRepository {

    private val listUsers = mutableListOf(
        UserDto(1, "Harry", "Potter", 13),
        UserDto(2, "Sirius", "Black", 33),
    )

    override suspend fun getUsers(): List<User> {
        return listUsers.map { dto ->
            User(
                name = dto.name,
                surname = dto.surname,
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
            surname = user.surname,
            age = user.age
        ))
    }

    override suspend fun updateUser(user: User) {
        val index = listUsers.indexOfFirst { it.id == user.id }
        if (index != -1) {
            listUsers[index] = UserDto(
                id = user.id,
                name = user.name,
                surname = user.surname,
                age = user.age
            )
        }
    }

    override suspend fun deleteUser(name: String, surname: String): Boolean {
        val initialSize = listUsers.size
        listUsers.removeAll { it.name == name && it.surname == surname }
        return listUsers.size < initialSize
    }
} 