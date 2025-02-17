package com.example.myapplication.presentaition.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.models.User
import com.example.myapplication.domain.usecases.UpdateUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UpdateUserViewModel(
    private val updateUserUseCase: UpdateUserUseCase
) : ViewModel() {
    private val _updateStatus = MutableStateFlow<Boolean?>(null)
    val updateStatus: StateFlow<Boolean?> = _updateStatus

    fun updateUser(user: User) = viewModelScope.launch {
        try {
            updateUserUseCase(user)
            _updateStatus.value = true
        } catch (e: Exception) {
            _updateStatus.value = false
        }
    }
} 