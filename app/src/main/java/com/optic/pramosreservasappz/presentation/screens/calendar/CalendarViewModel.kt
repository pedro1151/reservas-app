package com.optic.pramosreservasappz.presentation.screens.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.optic.pramosreservasappz.domain.model.reservas.clients.ClientResponse
import com.optic.pramosreservasappz.domain.model.reservas.staff.StaffResponse
import com.optic.pramosreservasappz.domain.repository.ReservasRepository
import com.optic.pramosreservasappz.domain.useCase.reservas.ReservasUC
import com.optic.pramosreservasappz.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val reservasUC: ReservasUC
) : ViewModel() {

    // ---------------------------------------------
    // STATE: Lista completa o filtrada según búsqueda
    // ---------------------------------------------
    private val _clientsState = MutableStateFlow<Resource<List<ClientResponse>>>(Resource.Loading)
    val clientsState: StateFlow<Resource<List<ClientResponse>>> = _clientsState

    // ---------------------------------------------
    // STATE: Lista completa o filtrada según búsqueda
    // ---------------------------------------------
    private val _staffState = MutableStateFlow<Resource<List<StaffResponse>>>(Resource.Loading)
    val staffState: StateFlow<Resource<List<StaffResponse>>> = _staffState



    // ---------------------------------------------
    // QUERY en StateFlow
    // ---------------------------------------------
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery


    init {
        observeSearch()
    }

    // ---------------------------------------------
    // OBSERVAR EL SEARCH QUERY
    // ---------------------------------------------
    private fun observeSearch() {
        viewModelScope.launch {
            _searchQuery
                .debounce(400)
                .distinctUntilChanged()
                .collectLatest { q ->
                    if (q.isBlank()) {
                        getClientsByProvider(1, "", "")
                    } else {
                        getClientsByProvider(
                            1,
                            fullName = q,
                            email = q
                        )
                    }
                }
        }
    }

    // Llamado desde el UI
    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    // ---------------------------------------------
    // API CALLS
    // ---------------------------------------------
    fun getClientsByProvider(
        providerId: Int,
        fullName:String,
        email:String
    ) {
        viewModelScope.launch {
            reservasUC.getClientPorProviderUC(providerId, fullName, email)
                .collectLatest { result ->
                    _clientsState.value = result
                }
        }
    }

    fun getStaffTotales(
    ) {
        viewModelScope.launch {
            reservasUC.getStaffTotalesUC()
                .collectLatest { result ->
                    _staffState.value = result
                }
        }
    }


}
