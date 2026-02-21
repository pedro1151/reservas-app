package com.optic.pramosreservasappz.presentation.screens.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.optic.pramosreservasappz.presentation.screens.calendar.components.ActivityItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class ActivityViewModel @Inject constructor(
    // TODO: inyectar repositorio de actividad
) : ViewModel() {

    private val _activities = MutableStateFlow<List<ActivityItem>>(emptyList())
    val activities: StateFlow<List<ActivityItem>> = _activities.asStateFlow()

    init {
        loadActivities()
    }

    private fun loadActivities() {
        viewModelScope.launch {
            // TODO: reemplazar con datos reales del API
            _activities.value = listOf(
                ActivityItem(
                    id = 1,
                    actorInitial = "P",
                    actorName = "Pedro",
                    action = "Perfil de cliente creado",
                    target = "Jonathan Ticona",
                    timestamp = LocalDateTime.now().minusMinutes(1)
                )
                // Cuando tengas más actividades del backend, agrégalas aquí
            )
        }
    }

    fun addActivity(item: ActivityItem) {
        _activities.value = listOf(item) + _activities.value
    }

    fun clearActivities() {
        _activities.value = emptyList()
    }
}

