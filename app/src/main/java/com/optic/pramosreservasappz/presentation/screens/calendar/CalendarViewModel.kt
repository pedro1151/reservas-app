package com.optic.pramosreservasappz.presentation.screens.calendar

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.optic.pramosreservasappz.domain.model.reservations.ReservationCreateRequest
import com.optic.pramosreservasappz.domain.model.reservations.ReservationResponse
import com.optic.pramosreservasappz.domain.model.reservations.ReservationUpdateRequest
import com.optic.pramosreservasappz.domain.useCase.reservas.ReservasUC
import com.optic.pramosreservasappz.domain.util.Resource
import com.optic.pramosreservasappz.presentation.screens.calendar.components.Reservation
import com.optic.pramosreservasappz.presentation.screens.calendar.components.ReservationStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val reservasUC: ReservasUC
) : ViewModel() {

    // ─────────────────────────────────────────────────────────────────────────
    //  Calendar navigation state
    // ─────────────────────────────────────────────────────────────────────────

    private val _currentMonth = MutableStateFlow(YearMonth.now())
    val currentMonth: StateFlow<YearMonth> = _currentMonth.asStateFlow()

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate.asStateFlow()

    /** Map of date -> reservation count, used to render dots on calendar cells */
    private val _reservationsByDate = MutableStateFlow<Map<LocalDate, Int>>(emptyMap())
    val reservationsByDate: StateFlow<Map<LocalDate, Int>> = _reservationsByDate.asStateFlow()

    /** Full reservation list for the currently selected day */
    private val _selectedDateReservations = MutableStateFlow<List<Reservation>>(emptyList())
    val selectedDateReservations: StateFlow<List<Reservation>> = _selectedDateReservations.asStateFlow()

    private val _weeklyEarnings = MutableStateFlow(0.0)
    val weeklyEarnings: StateFlow<Double> = _weeklyEarnings.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // ─────────────────────────────────────────────────────────────────────────
    //  New-reservation wizard form state
    //  (shared across ServiceReservationFlow steps via StateFlow)
    // ─────────────────────────────────────────────────────────────────────────

    private val _selectedServiceIdForReservation = MutableStateFlow<Int?>(null)
    val selectedServiceIdForReservation: StateFlow<Int?> = _selectedServiceIdForReservation.asStateFlow()

    private val _selectedClientIdForReservation = MutableStateFlow<Int?>(null)
    val selectedClientIdForReservation: StateFlow<Int?> = _selectedClientIdForReservation.asStateFlow()

    private val _selectedDateForReservation = MutableStateFlow<LocalDate?>(null)
    val selectedDateForReservation: StateFlow<LocalDate?> = _selectedDateForReservation.asStateFlow()

    private val _selectedTimeForReservation = MutableStateFlow<LocalTime?>(null)
    val selectedTimeForReservation: StateFlow<LocalTime?> = _selectedTimeForReservation.asStateFlow()

    private val _internalNoteForReservation = MutableStateFlow("")
    val internalNoteForReservation: StateFlow<String> = _internalNoteForReservation.asStateFlow()

    // ─────────────────────────────────────────────────────────────────────────
    //  Init
    // ─────────────────────────────────────────────────────────────────────────

    init {
        loadMonth(YearMonth.now())
        // TODO: API — al iniciar, cargar reservas del mes actual desde el servidor
        // getReservations()
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  Calendar navigation
    // ─────────────────────────────────────────────────────────────────────────

    fun loadMonth(month: YearMonth) {
        viewModelScope.launch {
            _currentMonth.value = month
            // TODO: API — reemplazar loadExampleData() con una llamada real:
            // reservasUC.getReservationsByMonthUC(month).collect { result ->
            //     when (result) {
            //         is Resource.Success -> _reservationsByDate.value = result.data
            //             .groupBy { LocalDate.parse(it.date) }
            //             .mapValues { it.value.size }
            //         is Resource.Failure -> { /* mostrar error */ }
            //         else -> {}
            //     }
            // }
            loadExampleData()
        }
    }

    fun selectDate(date: LocalDate) {
        _selectedDate.value = date
        loadReservationsForDate(date)
    }

    fun previousMonth() { loadMonth(_currentMonth.value.minusMonths(1)) }

    fun nextMonth()     { loadMonth(_currentMonth.value.plusMonths(1))  }

    fun goToToday() {
        val today = LocalDate.now()
        _currentMonth.value  = YearMonth.from(today)
        _selectedDate.value  = today
        loadReservationsForDate(today)
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  Reservations for a single day
    // ─────────────────────────────────────────────────────────────────────────

    private fun loadReservationsForDate(date: LocalDate) {
        viewModelScope.launch {
            // TODO: API — reemplazar datos de ejemplo con llamada real:
            // reservasUC.getReservationsByDateUC(date).collect { result ->
            //     when (result) {
            //         is Resource.Success -> {
            //             _selectedDateReservations.value = result.data.map { it.toUiModel() }
            //             _weeklyEarnings.value = result.data.sumOf { it.price ?: 0.0 }
            //         }
            //         is Resource.Failure -> { /* mostrar error */ }
            //         else -> {}
            //     }
            // }
            _selectedDateReservations.value = if (date == LocalDate.now()) {
                val list = getExampleReservations()
                _weeklyEarnings.value = list.sumOf { it.price }
                list
            } else {
                _weeklyEarnings.value = 0.0
                emptyList()
            }
        }
    }

    fun cancelReservation(reservationId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            // TODO: API — llamar reservasUC.deleteReservationUC(reservationId) antes de actualizar UI
            // reservasUC.deleteReservationUC(reservationId).collect { result ->
            //     when (result) {
            //         is Resource.Success -> { /* continuar con actualización local */ }
            //         is Resource.Failure -> { /* mostrar error */ }
            //         else -> {}
            //     }
            // }
            val updated = _selectedDateReservations.value.filter { it.id != reservationId }
            _selectedDateReservations.value = updated
            _weeklyEarnings.value = updated.sumOf { it.price }
            loadMonth(_currentMonth.value)
            _isLoading.value = false
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  Wizard form setters
    // ─────────────────────────────────────────────────────────────────────────

    fun setSelectedServiceForReservation(serviceId: Int?) { _selectedServiceIdForReservation.value = serviceId }
    fun setSelectedClientForReservation(clientId: Int?)   { _selectedClientIdForReservation.value  = clientId  }
    fun setSelectedDateForReservation(date: LocalDate?)   { _selectedDateForReservation.value       = date      }
    fun setSelectedTimeForReservation(time: LocalTime?)   { _selectedTimeForReservation.value       = time      }
    fun setInternalNoteForReservation(note: String)       { _internalNoteForReservation.value       = note      }

    fun clearReservationForm() {
        _selectedServiceIdForReservation.value = null
        _selectedClientIdForReservation.value  = null
        _selectedDateForReservation.value      = null
        _selectedTimeForReservation.value      = null
        _internalNoteForReservation.value      = ""
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  API use cases — Reservations
    // ─────────────────────────────────────────────────────────────────────────

    /** State exposed to UI for the full reservations list */
    private val _reservasState = MutableStateFlow<Resource<List<ReservationResponse>>>(Resource.Loading)
    val reservasState: StateFlow<Resource<List<ReservationResponse>>> = _reservasState.asStateFlow()

    /** State for the create-reservation operation */
    private val _createReservationState = MutableStateFlow<Resource<ReservationResponse>>(Resource.Idle)
    val createReservationState: StateFlow<Resource<ReservationResponse>> = _createReservationState.asStateFlow()

    /** State for the update-reservation operation */
    private val _updateReservationState = MutableStateFlow<Resource<ReservationResponse>>(Resource.Idle)
    val updateReservationState: StateFlow<Resource<ReservationResponse>> = _updateReservationState.asStateFlow()

    /** State for a single-reservation fetch */
    private val _oneReservationState = MutableStateFlow<Resource<ReservationResponse>>(Resource.Idle)
    val oneReservationState: StateFlow<Resource<ReservationResponse>> = _oneReservationState.asStateFlow()

    // TODO: API — llamar desde CalendarScreen en un LaunchedEffect al inicio
    fun getReservations() {
        viewModelScope.launch {
            reservasUC.getReservationsUC()
                .onStart  { _reservasState.value = Resource.Loading }
                .catch { e -> _reservasState.value = Resource.Failure(e.message ?: "Error al recuperar las reservas") }
                .collectLatest { _reservasState.value = it }
        }
    }

    // TODO: API — llamar desde ReservationDetailScreen o similar
    fun getReservationById(reservationId: Int) {
        viewModelScope.launch {
            reservasUC.getReservationByIdUC(reservationId)
                .onStart  { _oneReservationState.value = Resource.Loading }
                .catch { e -> _oneReservationState.value = Resource.Failure(e.message ?: "Error al recuperar la reserva") }
                .collectLatest { _oneReservationState.value = it }
        }
    }

    /**
     * TODO: API — llamar desde SummaryStep.onCreate, pasando:
     *   ReservationCreateRequest(
     *       serviceId = selectedServiceIdForReservation.value!!,
     *       clientId  = selectedClientIdForReservation.value!!,
     *       date      = selectedDateForReservation.value.toString(),   // "YYYY-MM-DD"
     *       time      = selectedTimeForReservation.value.toString(),   // "HH:mm"
     *       note      = internalNoteForReservation.value
     *   )
     * Observar createReservationState en la UI para mostrar loading/error.
     */
    fun createReservation(request: ReservationCreateRequest) {
        viewModelScope.launch {
            reservasUC.createReservationUC(request)
                .onStart  { _createReservationState.value = Resource.Loading }
                .catch { e -> _createReservationState.value = Resource.Failure(e.message ?: "Error al crear la reserva") }
                .collectLatest { result ->
                    _createReservationState.value = result
                    // Al tener éxito, refrescar el mes para mostrar el nuevo punto en el calendario
                    if (result is Resource.Success) {
                        loadMonth(_currentMonth.value)
                    }
                }
        }
    }

    // TODO: API — llamar desde pantalla de edición de reserva
    fun updateReservation(reservationId: Int, request: ReservationUpdateRequest) {
        viewModelScope.launch {
            reservasUC.updateReservationUC(reservationId, request)
                .onStart  { _updateReservationState.value = Resource.Loading }
                .catch { e -> _updateReservationState.value = Resource.Failure(e.message ?: "Error al actualizar reserva") }
                .collectLatest { result ->
                    _updateReservationState.value = result
                    if (result is Resource.Success) loadMonth(_currentMonth.value)
                }
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  Example / placeholder data (remove when real API is connected)
    // ─────────────────────────────────────────────────────────────────────────

    private fun loadExampleData() {
        _reservationsByDate.value = mapOf(
            LocalDate.now()             to 3,
            LocalDate.now().plusDays(1) to 2,
            LocalDate.now().plusDays(3) to 1,
            LocalDate.now().plusDays(7) to 5
        )
        loadReservationsForDate(_selectedDate.value)
    }

    private fun getExampleReservations(): List<Reservation> = listOf(
        Reservation(
            id = 1, clientName = "Gaston Edul",
            serviceName = "Peluquería Caballeros", serviceColor = Color(0xFFFF5722),
            startTime = LocalTime.of(10, 0), durationMinutes = 30,
            status = ReservationStatus.CONFIRMED, price = 25.50
        ),
        Reservation(
            id = 2, clientName = "Edgar Ramirez",
            serviceName = "Tintura de cabello", serviceColor = Color(0xFF2196F3),
            startTime = LocalTime.of(11, 30), durationMinutes = 60,
            status = ReservationStatus.PENDING, price = 35.00
        ),
        Reservation(
            id = 3, clientName = "Eduardo Abaroa",
            serviceName = "Masajes corporales", serviceColor = Color(0xFF4CAF50),
            startTime = LocalTime.of(15, 0), durationMinutes = 60,
            status = ReservationStatus.COMPLETED, price = 70.50
        )
    )
}
