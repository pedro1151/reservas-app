package com.optic.pramozventicoappz.presentation.screens.configuracion

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.optic.pramozventicoappz.core.Config
import com.optic.pramozventicoappz.domain.useCase.auth.AuthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MasViewModel @Inject constructor(
    private val authUseCase: AuthUseCase
) : ViewModel() {

    private val _selectedTheme = MutableStateFlow("Sistema")
    val selectedTheme: StateFlow<String> = _selectedTheme.asStateFlow()

    private val _selectedLanguage = MutableStateFlow("Español")
    val selectedLanguage: StateFlow<String> = _selectedLanguage.asStateFlow()

    private val _notificationsEnabled = MutableStateFlow(true)
    val notificationsEnabled: StateFlow<Boolean> = _notificationsEnabled.asStateFlow()

    private val _emailNotificationsEnabled = MutableStateFlow(true)
    val emailNotificationsEnabled: StateFlow<Boolean> = _emailNotificationsEnabled.asStateFlow()

    fun updateTheme(theme: String) {
        _selectedTheme.value = theme
    }

    fun updateLanguage(language: String) {
        _selectedLanguage.value = language
    }

    fun toggleNotifications() {
        _notificationsEnabled.value = !_notificationsEnabled.value
    }

    fun toggleEmailNotifications() {
        _emailNotificationsEnabled.value = !_emailNotificationsEnabled.value
    }

    fun logout(context: Context) {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(Config.CLIENT_ID_GOOGLE)
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(context, gso)

        googleSignInClient.signOut().addOnCompleteListener {
            viewModelScope.launch {
                authUseCase.logout()
            }
        }
    }
}