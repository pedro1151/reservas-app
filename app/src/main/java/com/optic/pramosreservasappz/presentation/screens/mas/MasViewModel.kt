package com.optic.pramosreservasappz.presentation.screens.mas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.optic.pramosreservasappz.domain.useCase.auth.AuthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MasViewModel @Inject constructor(
    // Aquí puedes inyectar dependencias si las necesitas
    private val authUseCase: AuthUseCase
) : ViewModel() {

    // 🎨 Estado del tema seleccionado
    private val _selectedTheme = MutableStateFlow("Sistema")
    val selectedTheme: StateFlow<String> = _selectedTheme.asStateFlow()

    // 🌐 Estado del idioma seleccionado
    private val _selectedLanguage = MutableStateFlow("Español")
    val selectedLanguage: StateFlow<String> = _selectedLanguage.asStateFlow()

    // 🔔 Estado de notificaciones
    private val _notificationsEnabled = MutableStateFlow(true)
    val notificationsEnabled: StateFlow<Boolean> = _notificationsEnabled.asStateFlow()

    // 📧 Estado de emails
    private val _emailNotificationsEnabled = MutableStateFlow(true)
    val emailNotificationsEnabled: StateFlow<Boolean> = _emailNotificationsEnabled.asStateFlow()

    // Funciones para actualizar estados
    fun updateTheme(theme: String) {
        _selectedTheme.value = theme
        // Aquí puedes implementar la lógica para cambiar el tema
    }

    fun updateLanguage(language: String) {
        _selectedLanguage.value = language
        // Aquí puedes implementar la lógica para cambiar el idioma
    }

    fun toggleNotifications() {
        _notificationsEnabled.value = !_notificationsEnabled.value
        // Aquí puedes implementar la lógica para activar/desactivar notificaciones
    }

    fun toggleEmailNotifications() {
        _emailNotificationsEnabled.value = !_emailNotificationsEnabled.value
        // Aquí puedes implementar la lógica para activar/desactivar emails
    }

    fun logout() = viewModelScope.launch {
        authUseCase.logout()
    }

}
