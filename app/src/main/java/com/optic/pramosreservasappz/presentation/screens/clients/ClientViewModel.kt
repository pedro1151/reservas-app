package com.optic.pramosreservasappz.presentation.screens.clients

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.optic.pramosreservasappz.domain.model.reservas.clients.ClientResponse
import com.optic.pramosreservasappz.domain.repository.ReservasRepository
import com.optic.pramosreservasappz.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClientViewModel @Inject constructor(
    private val reservasRepository: ReservasRepository
) : ViewModel() {

    // ---------------------------------------------
    // STATE: Lista completa o filtrada según búsqueda
    // ---------------------------------------------
    private val _clientsState = MutableStateFlow<Resource<List<ClientResponse>>>(Resource.Loading)
    val clientsState: StateFlow<Resource<List<ClientResponse>>> = _clientsState


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
                        getClientsByProvider(1)
                    } else {
                        getClientsByProvider(1)
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
        providerId: Int
    ) {
        viewModelScope.launch {
            reservasRepository.getClientsByProvider(providerId)
                .collectLatest { result ->
                    _clientsState.value = result
                }
        }
    }


}
