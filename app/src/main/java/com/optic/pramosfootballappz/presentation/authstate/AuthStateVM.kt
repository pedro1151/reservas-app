package com.optic.pramosfootballappz.presentation.authstate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.optic.pramosfootballappz.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AuthStateVM @Inject constructor(
    authRepository: AuthRepository
) : ViewModel() {

    // VARIABLE QUE SE USARA EN TODA LA APP PARA MANEJAR EL ESTADO DE LOGIN
    //DEL USUARIO Y DEMAS COSAS QUE TENGAN QUE VER CON LA AUTENTICACION

    val isAuthenticated: StateFlow<Boolean> = authRepository.isUserLoggedIn()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    val userEmail = authRepository.getSessionData()
        .map { it.user?.email ?: "" }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")
}