package com.optic.pramosreservasappz.presentation.screens.clients

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.optic.pramosreservasappz.domain.model.reservas.clients.ClientCreateRequest
import com.optic.pramosreservasappz.domain.model.reservas.clients.ClientResponse
import com.optic.pramosreservasappz.domain.model.reservas.clients.ClientUpdateRequest
import com.optic.pramosreservasappz.domain.model.reservas.services.ServiceCreateRequest
import com.optic.pramosreservasappz.domain.model.reservas.services.ServiceResponse
import com.optic.pramosreservasappz.domain.model.response.DefaultResponse
import com.optic.pramosreservasappz.domain.repository.ReservasRepository
import com.optic.pramosreservasappz.domain.useCase.reservas.ReservasUC
import com.optic.pramosreservasappz.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onStart
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


    // ---------------------------------------------
    // STATE: get client por id state
    // ---------------------------------------------
    private val _deleteClientState =
        mutableStateOf<Resource<DefaultResponse>>(Resource.Loading)

    val deleteClientState: State<Resource<DefaultResponse>> = _deleteClientState

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
            reservasUC.getClientPorProviderUC(
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

    // get cliente por ID
    private fun getClientById(clientId: Int) {
        viewModelScope.launch {

            reservasUC.getClientPorIdUC(clientId)
                .onStart {

                    _oneClientState.value = Resource.Loading
                }
                .catch { e ->
                    _oneClientState.value = Resource.Failure(e.message ?: "Error desconocido")
                }
                .collectLatest { result ->
                    _oneClientState.value = result
                }
        }
    }


    // crear cliente
    private fun createClient(
        request: ClientCreateRequest
    ) {
        viewModelScope.launch {

            reservasUC.createClientUC(request)
                .onStart {

                    _createClientState.value = Resource.Loading
                }
                .catch { e ->
                    _createClientState.value = Resource.Failure(e.message ?: "Error desconocido")
                }
                .collectLatest { result ->
                    _createClientState.value = result
                }
        }
    }


    // update client
    private fun updateClient(
        clientId: Int,
        request: ClientUpdateRequest
    ) {
        viewModelScope.launch {

            reservasUC.updateClientUC(clientId, request)
                .onStart {

                    _updateClientState.value = Resource.Loading
                }
                .catch { e ->
                    _updateClientState.value  = Resource.Failure(e.message ?: "Error desconocido")
                }
                .collectLatest { result ->
                    _updateClientState.value  = result
                }
        }
    }


    // update client
    // ---------------------------------------------
    // CREATE SERVICE
    // ---------------------------------------------
    private fun deleteClient(clientId: Int) {
        viewModelScope.launch {
            _deleteClientState.value = Resource.Loading

            try {
                val response = reservasUC.deleteClientUC(clientId)
                _deleteClientState.value = response
            } catch (e: Exception) {
                _deleteClientState.value =
                    Resource.Failure(e.localizedMessage ?: "Error al eliminar cliente")
            }
        }
    }

}
