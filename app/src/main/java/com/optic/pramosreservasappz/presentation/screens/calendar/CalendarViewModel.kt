package com.optic.pramosreservasappz.presentation.screens.calendar

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.optic.pramosreservasappz.presentation.screens.calendar.components.Reservation
import com.optic.pramosreservasappz.presentation.screens.calendar.components.ReservationStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    // TODO: Inyectar tu UseCase de reservas cuando lo tengas
    // private val reservationsUC: ReservationsUseCase
) : ViewModel() {

    private val _currentMonth = MutableStateFlow(YearMonth.now())
    val currentMonth: StateFlow<YearMonth> = _currentMonth.asStateFlow()

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate.asStateFlow()

    private val _reservationsByDate = MutableStateFlow<Map<LocalDate, Int>>(emptyMap())
    val reservationsByDate: StateFlow<Map<LocalDate, Int>> = _reservationsByDate.asStateFlow()

    private val _selectedDateReservations = MutableStateFlow<List<Reservation>>(emptyList())
    val selectedDateReservations: StateFlow<List<Reservation>> = _selectedDateReservations.asStateFlow()

    private val _weeklyEarnings = MutableStateFlow(0.0)
    val weeklyEarnings: StateFlow<Double> = _weeklyEarnings.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadMonth(YearMonth.now())
    }

    fun loadMonth(month: YearMonth) {
        viewModelScope.launch {
            _currentMonth.value = month
            loadExampleData()
        }
    }

    fun selectDate(date: LocalDate) {
        _selectedDate.value = date
        loadReservationsForDate(date)
    }

    fun previousMonth() {
        loadMonth(_currentMonth.value.minusMonths(1))
    }

    fun nextMonth() {
        loadMonth(_currentMonth.value.plusMonths(1))
    }

    fun goToToday() {
        val today = LocalDate.now()
        _currentMonth.value = YearMonth.from(today)
        _selectedDate.value = today
        loadReservationsForDate(today)
    }

    private fun loadReservationsForDate(date: LocalDate) {
        viewModelScope.launch {
            _selectedDateReservations.value = if (date == LocalDate.now()) {
                val reservations = getExampleReservations()
                _weeklyEarnings.value = reservations.sumOf { it.price }
                reservations
            } else {
                _weeklyEarnings.value = 0.0
                emptyList()
            }
        }
    }

    fun cancelReservation(reservationId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            val updated = _selectedDateReservations.value.filter { it.id != reservationId }
            _selectedDateReservations.value = updated
            _weeklyEarnings.value = updated.sumOf { it.price }
            loadMonth(_currentMonth.value)
            _isLoading.value = false
        }
    }

    private fun loadExampleData() {
        _reservationsByDate.value = mapOf(
            LocalDate.now() to 3,
            LocalDate.now().plusDays(1) to 2,
            LocalDate.now().plusDays(3) to 1,
            LocalDate.now().plusDays(7) to 5
        )
        loadReservationsForDate(_selectedDate.value)
    }

    private fun getExampleReservations(): List<Reservation> {
        return listOf(
            Reservation(
                id = 1,
                clientName = "Gaston Edul",
                serviceName = "Peluquería Caballeros",
                serviceColor = Color(0xFFFF5722),
                startTime = java.time.LocalTime.of(10, 0),
                durationMinutes = 30,
                status = ReservationStatus.CONFIRMED,
                price = 25.50
            ),
            Reservation(
                id = 2,
                clientName = "Edgar Ramirez",
                serviceName = "Tintura de cabello",
                serviceColor = Color(0xFF2196F3),
                startTime = java.time.LocalTime.of(11, 30),
                durationMinutes = 60,
                status = ReservationStatus.PENDING,
                price = 35.00
            ),
            Reservation(
                id = 3,
                clientName = "Eduardo Abaroa",
                serviceName = "Masajes corporales",
                serviceColor = Color(0xFF4CAF50),
                startTime = java.time.LocalTime.of(15, 0),
                durationMinutes = 60,
                status = ReservationStatus.COMPLETED,
                price = 70.50
            )
        )
    }
}
