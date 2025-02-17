package com.example.myapplication.presentaition.ui.activities

import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.R
import com.example.myapplication.data.repositories.FakeUserRepository
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.domain.models.User
import com.example.myapplication.domain.usecases.AddUserUseCase
import com.example.myapplication.domain.usecases.GetUsersUseCase
import com.example.myapplication.domain.usecases.UpdateUserUseCase
import com.example.myapplication.presentaition.ui.fragments.UserProfileFragment
import com.example.myapplication.presentaition.viewmodelfactories.AddUserViewModelFactory
import com.example.myapplication.presentaition.viewmodelfactories.UserViewModelFactory
import com.example.myapplication.presentaition.viewmodelfactories.UpdateUserViewModelFactory
import com.example.myapplication.presentaition.viewmodels.AddUserViewModel
import com.example.myapplication.presentaition.viewmodels.UserViewModel
import com.example.myapplication.presentaition.viewmodels.UpdateUserViewModel

@Suppress("UNREACHABLE_CODE", "DEPRECATION")
class MainActivity : AppCompatActivity() {
    private lateinit var userViewModel: UserViewModel
    private lateinit var addUserViewModel: AddUserViewModel
    private lateinit var updateUserViewModel: UpdateUserViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userRepository = FakeUserRepository()
        val addUserUseCase = AddUserUseCase(userRepository)
        val getUsersUseCase = GetUsersUseCase(userRepository)
        val updateUserUseCase = UpdateUserUseCase(userRepository)

        val viewModelFactory = UserViewModelFactory(getUsersUseCase)
        val addUserViewModelFactory = AddUserViewModelFactory(addUserUseCase)
        val updateUserViewModelFactory = UpdateUserViewModelFactory(updateUserUseCase)

        userViewModel = ViewModelProvider(this, viewModelFactory)[UserViewModel::class.java]
        addUserViewModel = ViewModelProvider(this, addUserViewModelFactory)[AddUserViewModel::class.java]
        updateUserViewModel = ViewModelProvider(this, updateUserViewModelFactory)[UpdateUserViewModel::class.java]

        binding.buttonId.setOnClickListener {
            val name = binding.editNameId.text.toString()
            val age = binding.editAgeId.text.toString()

            if (name.isNotEmpty() && age.isNotEmpty()) {
                addUserViewModel.addUser(User(name = name, age = age.toInt(), id = 0))
            }
        }

        binding.updateButtonId.setOnClickListener {
            val id = binding.editIdId.text.toString().toIntOrNull()
            val name = binding.editNameId.text.toString()
            val age = binding.editAgeId.text.toString().toIntOrNull()

            if (id != null && name.isNotEmpty() && age != null) {
                updateUserViewModel.updateUser(User(name = name, age = age, id = id))
            }
        }

        lifecycleScope.launchWhenResumed {
            userViewModel.users.collect { users ->
                displayUsers(users)
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
            sb.append("ID: ${user.id}, Имя: ${user.name}, Возраст: ${user.age}\n")
        }
        binding.surnameId.text = sb.toString()
    }
}
