package com.optic.pramosreservasappz.presentation.screens.services

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.optic.pramosreservasappz.domain.model.reservas.services.ServiceResponse
import com.optic.pramosreservasappz.domain.repository.ReservasRepository
import com.optic.pramosreservasappz.domain.useCase.reservas.ReservasUC
import com.optic.pramosreservasappz.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ServiceViewModel @Inject constructor(
    private val reservasUC: ReservasUC
) : ViewModel() {

    // ---------------------------------------------
    // STATE: Lista completa o filtrada según búsqueda
    // ---------------------------------------------
    private val _servicesState = MutableStateFlow<Resource<List<ServiceResponse>>>(Resource.Loading)
    val servicesState: StateFlow<Resource<List<ServiceResponse>>> = _servicesState


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
                        getServicesByProvider(1)
                    } else {
                        getServicesByProvider(
                            providerId = 1,
                            name = q
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
    fun getServicesByProvider(
        providerId: Int,
        name: String = ""
    ) {
        viewModelScope.launch {
            reservasUC.getServicesPorProviderUC(providerId, name)
                .collectLatest { result ->
                    _servicesState.value = result
                }
        }
    }

}
