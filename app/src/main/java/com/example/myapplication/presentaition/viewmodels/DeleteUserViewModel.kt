package com.example.myapplication.presentaition.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.usecases.DeleteUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DeleteUserViewModel(
    private val deleteUserUseCase: DeleteUserUseCase
) : ViewModel() {
    private val _deleteStatus = MutableStateFlow<DeleteResult?>(null)
    val deleteStatus: StateFlow<DeleteResult?> = _deleteStatus

    fun deleteUser(name: String, surname: String) = viewModelScope.launch {
        try {
            val isDeleted = deleteUserUseCase(name, surname)
            _deleteStatus.value = if (isDeleted) {
                DeleteResult.Success
            } else {
                DeleteResult.NotFound
            }
        } catch (e: Exception) {
            _deleteStatus.value = DeleteResult.Error(e.message ?: "Неизвестная ошибка")
        }
    }
}

sealed class DeleteResult {
    object Success : DeleteResult()
    object NotFound : DeleteResult()
    data class Error(val message: String) : DeleteResult()
} 