package com.optic.pramozventicoappz.presentation.screens.estadisticas

import com.optic.pramozventicoappz.domain.model.sales.SalesStatsResponse
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.optic.pramozventicoappz.domain.useCase.reservas.ReservasUC
import com.optic.pramozventicoappz.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SaleStatsViewModel @Inject constructor(
    private val reservasUC: ReservasUC
) : ViewModel() {

    // ---------------------------------------------
    // STATE: Lista de ventas
    // ---------------------------------------------
    private val _salesStatsState =
        MutableStateFlow<Resource<SalesStatsResponse>>(Resource.Loading)
    val salesStatsState: StateFlow<Resource<SalesStatsResponse>> =
        _salesStatsState.asStateFlow()


    // ---------------------------------------------
    // LOAD SALES (Flow ✅)
    // ---------------------------------------------
    fun loadStats(
        businessId: Int,
        year: Int
    ) {
        viewModelScope.launch {
            reservasUC.getSaleStatsUC(
                businessId = businessId,
                year = year
            )
                .onStart {
                    _salesStatsState.value = Resource.Loading
                    delay(200) // 🔥 pequeño delay UX
                }
                .catch { e ->
                    _salesStatsState.value =
                        Resource.Failure(e.message ?: "Error al cargar ventas")
                }
                .collectLatest { result ->
                    _salesStatsState.value = result

                }
        }
    }


    // 🔥 Año seleccionado
    private val _selectedYear = MutableStateFlow(
        java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)
    )
    val selectedYear: StateFlow<Int> = _selectedYear.asStateFlow()

    fun setYear(year: Int) {
        _selectedYear.value = year
        loadStats(businessId = 1, year = year) // 🔥 hardcode por ahora
    }




    // 🔥 MODOS DISPONIBLES
    enum class StatsMode {
        FINTECH,
        FIRE,
        ANALITIC,
        GAMMING
    }

    // 🔥 STATE DEL MODO
    private val _selectedMode = MutableStateFlow(StatsMode.FIRE)
    val selectedMode: StateFlow<StatsMode> = _selectedMode.asStateFlow()

    fun setMode(mode: StatsMode) {
        _selectedMode.value = mode
    }
}
