package com.example.myapplication.presentaition.ui.activities

import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.R
import com.example.myapplication.data.repositories.FakeUserRepository
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.domain.models.User
import com.example.myapplication.domain.usecases.AddUserUseCase
import com.example.myapplication.domain.usecases.DeleteUserUseCase
import com.example.myapplication.domain.usecases.GetUsersUseCase
import com.example.myapplication.domain.usecases.UpdateUserUseCase
import com.example.myapplication.presentaition.ui.fragments.UserProfileFragment
import com.example.myapplication.presentaition.viewmodelfactories.AddUserViewModelFactory
import com.example.myapplication.presentaition.viewmodelfactories.DeleteUserViewModelFactory
import com.example.myapplication.presentaition.viewmodelfactories.UserViewModelFactory
import com.example.myapplication.presentaition.viewmodelfactories.UpdateUserViewModelFactory
import com.example.myapplication.presentaition.viewmodels.AddUserViewModel
import com.example.myapplication.presentaition.viewmodels.DeleteResult
import com.example.myapplication.presentaition.viewmodels.DeleteUserViewModel
import com.example.myapplication.presentaition.viewmodels.UpdateUserViewModel
import com.example.myapplication.presentaition.viewmodels.UserViewModel
import kotlinx.coroutines.launch

@Suppress("UNREACHABLE_CODE", "DEPRECATION")
class MainActivity : AppCompatActivity() {
    private lateinit var userViewModel: UserViewModel
    private lateinit var addUserViewModel: AddUserViewModel
    private lateinit var updateUserViewModel: UpdateUserViewModel
    private lateinit var deleteUserViewModel: DeleteUserViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModels()
        setupClickListeners()
        observeUsers()
        observeDeleteStatus()
    }

    private fun setupViewModels() {
        val userRepository = FakeUserRepository()
        val addUserUseCase = AddUserUseCase(userRepository)
        val getUsersUseCase = GetUsersUseCase(userRepository)
        val updateUserUseCase = UpdateUserUseCase(userRepository)
        val deleteUserUseCase = DeleteUserUseCase(userRepository)

        userViewModel = ViewModelProvider(this, UserViewModelFactory(getUsersUseCase))[UserViewModel::class.java]
        addUserViewModel = ViewModelProvider(this, AddUserViewModelFactory(addUserUseCase))[AddUserViewModel::class.java]
        updateUserViewModel = ViewModelProvider(this, UpdateUserViewModelFactory(updateUserUseCase))[UpdateUserViewModel::class.java]
        deleteUserViewModel = ViewModelProvider(this, DeleteUserViewModelFactory(deleteUserUseCase))[DeleteUserViewModel::class.java]
    }

    private fun setupClickListeners() {
        binding.buttonId.setOnClickListener {
            val name = binding.editNameId.text.toString()
            val surname = binding.editSurnameId.text.toString()
            val age = binding.editAgeId.text.toString()

            if (name.isNotEmpty() && surname.isNotEmpty() && age.isNotEmpty()) {
                lifecycleScope.launch {
                    addUserViewModel.addUser(User(
                        name = name,
                        surname = surname,
                        age = age.toInt(),
                        id = 0
                    ))
                    userViewModel.fetchUsers()
                }
            }
        }

        binding.updateButtonId.setOnClickListener {
            val id = binding.editIdId.text.toString().toIntOrNull()
            val name = binding.editNameId.text.toString()
            val surname = binding.editSurnameId.text.toString()
            val age = binding.editAgeId.text.toString().toIntOrNull()

            if (id != null && name.isNotEmpty() && surname.isNotEmpty() && age != null) {
                lifecycleScope.launch {
                    updateUserViewModel.updateUser(User(
                        name = name,
                        surname = surname,
                        age = age,
                        id = id
                    ))
                    userViewModel.fetchUsers()
                }
            }
        }

        binding.deleteButtonId.setOnClickListener {
            val name = binding.editNameId.text.toString()
            val surname = binding.editSurnameId.text.toString()

            if (name.isNotEmpty() && surname.isNotEmpty()) {
                deleteUserViewModel.deleteUser(name, surname)
            }
        }
    }

    private fun observeUsers() {
        lifecycleScope.launchWhenStarted {
            userViewModel.users.collect { users ->
                displayUsers(users)
            }
        }
    }

    private fun observeDeleteStatus() {
        lifecycleScope.launchWhenStarted {
            deleteUserViewModel.deleteStatus.collect { result ->
                when (result) {
                    is DeleteResult.Success -> {
                        userViewModel.fetchUsers()
                        showToast("Пользователь успешно удален")
                    }
                    is DeleteResult.NotFound -> {
                        showToast("Пользователь не найден")
                    }
                    is DeleteResult.Error -> {
                        showToast("Ошибка: ${result.message}")
                    }
                    null -> {
                        // Начальное состояние, ничего не делаем
                    }
                }
            }
        }
    }

    private fun addFragment(fragment: Fragment, containerId: Int?, savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            if (supportFragmentManager.findFragmentById(containerId!!) == null) {
                supportFragmentManager
                    .beginTransaction()
                    .add(containerId, fragment)
                    .commitAllowingStateLoss()
            }
        }
    }

    private fun replaceFragment(fragment: Fragment, containerId: Int) {
        if (supportFragmentManager.findFragmentById(containerId) == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(containerId, fragment)
                .commitAllowingStateLoss()
        }
    }

    private fun removeFragment(fragment: Fragment, containerId: Int) {
        if (supportFragmentManager.findFragmentById(containerId) == null) {
            supportFragmentManager
                .beginTransaction()
                .remove(fragment)
                .commitAllowingStateLoss()
        }
    }

    private fun displayUsers(users: List<User>) {
        val sb = StringBuilder()
        users.forEach { user ->
            sb.append("ID: ${user.id}, Имя: ${user.name}, Фамилия: ${user.surname}, Возраст: ${user.age}\n")
        }
        binding.userListTextView.text = sb.toString()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
