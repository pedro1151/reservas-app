!@@package com.optic.pramosreservasappz.presentation.screens.clients

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.optic.pramosreservasappz.domain.model.reservas.clients.ClientCreateRequest
import com.optic.pramosreservasappz.domain.model.reservas.clients.ClientResponse
import com.optic.pramosreservasappz.domain.model.reservas.clients.ClientUpdateRequest
import com.optic.pramosreservasappz.domain.model.reservas.services.ServiceResponse
import com.optic.pramosreservasappz.domain.repository.ReservasRepository
import com.optic.pramosreservasappz.domain.useCase.reservas.ReservasUC
import com.optic.pramosreservasappz.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClientViewModel @Inject constructor(
    private val reservasUC: ReservasUC
) : ViewModel() {

    private val _clientsState =
        MutableStateFlow<Resource<List<ClientResponse>>>(Resource.Loading)
    val clientsState: StateFlow<Resource<List<ClientResponse>>> = _clientsState

    // ---------------------------------------------
    // create client state
    // ---------------------------------------------
    private val _createClientState = MutableStateFlow<Resource<ClientResponse>>(Resource.Idle)
    val createClientState: StateFlow<Resource<ClientResponse>> = _createClientState

    // ---------------------------------------------
    // update client state
    // ---------------------------------------------
    private val _updateClientState = MutableStateFlow<Resource<ClientResponse>>(Resource.Idle)
    val updateClientState: StateFlow<Resource<ClientResponse>> = _updateClientState


    // ---------------------------------------------
    // STATE: get client por id state
    // ---------------------------------------------
    private val _oneClientState = MutableStateFlow<Resource<ClientResponse>>(Resource.Loading)
    val oneClientState: StateFlow<Resource<ClientResponse>> = _oneClientState



    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    init {
       observeSearch()
    }

    private fun loadClients(
        fullName: String,
        email: String,
        providerId:Int
    ) {
        viewModelScope.launch {
            reservasRepository.getClientsByProvider(
                providerId = 1,
                fullName = fullName ,
                email = email
            )
                .collect {
                    _clientsState.value = it
                }
        }
    }


    private fun observeSearch() {
        viewModelScope.launch {
            _searchQuery
                .debounce(400)
                .distinctUntilChanged()
                .collectLatest { q ->
                    if (q.isBlank()) {
                        loadClients(
                            providerId = 1,
                            fullName = "",
                            email =  "",

                        )
                    } else {
                        loadClients(
                            providerId = 1,
                            fullName = q,
                            email =  q

                            )
                    }
                }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }
}
