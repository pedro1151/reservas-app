package com.optic.pramosreservasappz.presentation.screens.calendar

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.optic.pramosreservasappz.domain.model.reservas.clients.ClientCreateRequest
import com.optic.pramosreservasappz.domain.model.reservas.clients.ClientResponse
import com.optic.pramosreservasappz.domain.model.reservas.clients.ClientUpdateRequest
import com.optic.pramosreservasappz.domain.model.reservas.reservations.ReservationCreateRequest
import com.optic.pramosreservasappz.domain.model.reservas.reservations.ReservationResponse
import com.optic.pramosreservasappz.domain.model.reservas.reservations.ReservationUpdateRequest
import com.optic.pramosreservasappz.domain.model.response.DefaultResponse
import com.optic.pramosreservasappz.domain.useCase.reservas.ReservasUC
import com.optic.pramosreservasappz.domain.util.Resource
import com.optic.pramosreservasappz.presentation.screens.calendar.components.Reservation
import com.optic.pramosreservasappz.presentation.screens.calendar.components.ReservationStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    // TODO: Inyectar tu UseCase de reservas cuando lo tengas
    private val reservasUC: ReservasUC
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




    // casos de uso reales  (pedro) para implementar en las pantallas de reservas

    // ---------------------------------------------
    // STATE: Lista de reservas: cada vez q se crea una reserva llamar en launched effect a esta lista
    // ---------------------------------------------
    private val _reservasState = MutableStateFlow<Resource<List<ReservationResponse>>>(Resource.Loading)
    val reservasState : StateFlow<Resource<List<ReservationResponse>>> = _reservasState .asStateFlow()



    // ---------------------------------------------
    // STATE: Crear uan reserva
    // ---------------------------------------------
    private val _createReservationState = MutableStateFlow<Resource<ReservationResponse>>(Resource.Idle)
    val createReservationState: StateFlow<Resource<ReservationResponse>> = _createReservationState.asStateFlow()

    // ---------------------------------------------
    // STATE: Actualizar reserva
    // ---------------------------------------------
    private val _updateReservationState = MutableStateFlow<Resource<ReservationResponse>>(Resource.Idle)
    val updateReservationState : StateFlow<Resource<ReservationResponse>> = _updateReservationState.asStateFlow()

    // ---------------------------------------------
    // STATE: Obtener una reserva por ID
    // ---------------------------------------------
    private val _oneReservationState =MutableStateFlow<Resource<ReservationResponse>>(Resource.Idle)
    val oneReservationState: StateFlow<Resource<ReservationResponse>> = _oneReservationState.asStateFlow()


    // ---------------------------------------------
    // FUNCIÓN: Obtener todas las reservas
    // ---------------------------------------------
    fun getReservations(
    ) {
        viewModelScope.launch {
            reservasUC.getReservationsUC()
                .onStart {
                    _reservasState.value = Resource.Loading
                }
                .catch { e ->
                    _reservasState.value = Resource.Failure(e.message ?: "Error al recuperar las reservas")
                }
                .collectLatest { result ->
                    _reservasState.value = result
                }
        }
    }




    // ---------------------------------------------
    // FUNCIÓN: Obtener cliente por ID
    // ---------------------------------------------
    fun getReservationById(
        reservationId: Int,
    ) {
        viewModelScope.launch {
            reservasUC.getReservationByIdUC(reservationId)
                .onStart {
                    _oneReservationState.value = Resource.Loading
                }
                .catch { e ->
                    _oneReservationState.value = Resource.Failure(e.message ?: "Error al recuperar la reserva por id")
                }
                .collectLatest { result ->
                    _oneReservationState.value = result
                }
        }
    }

    // ---------------------------------------------
    // FUNCIÓN: Crear REserva
    // ---------------------------------------------
    fun createReservation(
        request: ReservationCreateRequest
    ) {
        viewModelScope.launch {
            reservasUC.createReservationUC(request)
                .onStart {
                    _createReservationState.value = Resource.Loading
                }
                .catch { e ->
                    _createReservationState.value = Resource.Failure(e.message ?: "Error al crear la reserva")
                }
                .collectLatest { result ->
                    _createReservationState.value = result

                }
        }
    }

    // ---------------------------------------------
    // FUNCIÓN: actualizar reserva
    // ---------------------------------------------
    fun updateReservation(
        reservationId: Int,
        request: ReservationUpdateRequest
    ) {
        viewModelScope.launch {
            reservasUC.updateReservationUC(reservationId, request)
                .onStart {
                    _updateReservationState.value = Resource.Loading
                }
                .catch { e ->
                    _updateReservationState.value = Resource.Failure(e.message ?: "Error al actualizar cliente")
                }
                .collectLatest { result ->
                    _updateReservationState.value = result

                }
        }
    }
}
