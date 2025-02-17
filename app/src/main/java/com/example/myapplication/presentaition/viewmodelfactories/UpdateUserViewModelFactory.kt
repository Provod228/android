package com.example.myapplication.presentaition.viewmodelfactories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.domain.usecases.UpdateUserUseCase
import com.example.myapplication.presentaition.viewmodels.UpdateUserViewModel

class UpdateUserViewModelFactory(
    private val updateUserUseCase: UpdateUserUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UpdateUserViewModel(updateUserUseCase) as T
    }
} 