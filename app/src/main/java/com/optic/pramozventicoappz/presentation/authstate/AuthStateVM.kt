package com.optic.pramozventicoappz.presentation.authstate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.optic.pramozventicoappz.domain.model.auth.SessionUserData
import com.optic.pramozventicoappz.domain.repository.AuthRepository
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

    val isAuthenticated: StateFlow<Boolean> = authRepository.isUserLoggedIn()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    val sessionData: StateFlow<SessionUserData> = authRepository.getSessionData()
        .map { auth ->
            SessionUserData(
                email = auth.user?.email?:"",
                userId = auth.user?.id,
                businessId = auth.business?.id,
                planCode = auth.planCode,
                businessName = auth.businessName,
                isOwner = auth.isOwner,
                isCollaborator = auth.isCollaborator,
                isAdmin = auth.isAdmin
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SessionUserData()
        )
}