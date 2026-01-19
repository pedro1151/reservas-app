package com.optic.pramosfootballappz.presentation.screens.auth.register

import android.util.Patterns
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.optic.pramosfootballappz.domain.model.AuthResponse
import com.optic.pramosfootballappz.domain.useCase.auth.AuthUseCase
import com.optic.pramosfootballappz.domain.util.Resource
import com.optic.pramosfootballappz.presentation.screens.auth.register.mapper.toUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val authUseCase: AuthUseCase): ViewModel() {

    var state by mutableStateOf(RegisterState())
        private set
    var registerResponse by mutableStateOf<Resource<AuthResponse>?>(null)
        private set
    var errorMessage by mutableStateOf("")

    fun saveSession(authResponse: AuthResponse) = viewModelScope.launch {
        authUseCase.saveSession(authResponse)
    }

    fun register() = viewModelScope.launch {
        if (isValidForm()) {
            registerResponse = Resource.Loading
            val result = authUseCase.register(state.toUser())
            registerResponse = result // DATA / ERROR
        }
    }

    fun onNameInput(input: String) {
        state = state.copy(username = input)
    }


    fun onEmailInput(input: String) {
        state = state.copy(email = input)
    }

    fun onPasswordInput(input: String) {
        state = state.copy(password = input)
    }

    fun onConfirmPasswordInput(input: String) {
        state = state.copy(confirmPassword = input)
    }

    fun isValidForm(): Boolean {

        if (state.username == "") {
            errorMessage = "Ingresa el nombre"
            return false
        }

        else if (state.email == "") {
            errorMessage = "Ingresa el email"
            return false
        }

        else if (state.password == "") {
            errorMessage = "Ingresa el password"
            return false
        }
        else if (state.confirmPassword == "") {
            errorMessage = "Ingresa el password de confirmacion"
            return false
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(state.email).matches()) {
            errorMessage = "El email no es valido"
            return false
        }
        else if (state.password.length < 6) {
            errorMessage = "La contraseña debe tener al menos 6 caracteres"
            return false
        }
        else if (state.password != state.confirmPassword) {
            errorMessage = "Las contraseñas no coinciden"
            return false
        }

        return true
    }

}