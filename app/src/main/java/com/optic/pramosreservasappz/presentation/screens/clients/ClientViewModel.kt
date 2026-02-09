package com.optic.pramosreservasappz.presentation.screens.clients

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.optic.pramosreservasappz.domain.model.reservas.clients.ClientCreateRequest
import com.optic.pramosreservasappz.domain.model.reservas.clients.ClientResponse
import com.optic.pramosreservasappz.domain.model.reservas.clients.ClientUpdateRequest
import com.optic.pramosreservasappz.domain.model.response.DefaultResponse
import com.optic.pramosreservasappz.domain.useCase.reservas.ReservasUC
import com.optic.pramosreservasappz.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.util.Log


@HiltViewModel
class ClientViewModel @Inject constructor(
    private val reservasUC: ReservasUC
) : ViewModel() {

    // ---------------------------------------------
    // STATE: Lista de clientes
    // ---------------------------------------------
    private val _clientsState = MutableStateFlow<Resource<List<ClientResponse>>>(Resource.Loading)
    val clientsState: StateFlow<Resource<List<ClientResponse>>> = _clientsState.asStateFlow()

    // 🔹 NUEVO: Lista mutable para control local
    private val _localClientsList = MutableStateFlow<List<ClientResponse>>(emptyList())
    val localClientsList: StateFlow<List<ClientResponse>> = _localClientsList.asStateFlow()

    // ---------------------------------------------
    // STATE: Crear cliente
    // ---------------------------------------------
    private val _createClientState = MutableStateFlow<Resource<ClientResponse>>(Resource.Idle)
    val createClientState: StateFlow<Resource<ClientResponse>> = _createClientState.asStateFlow()

    // ---------------------------------------------
    // STATE: Actualizar cliente
    // ---------------------------------------------
    private val _updateClientState = MutableStateFlow<Resource<ClientResponse>>(Resource.Idle)
    val updateClientState: StateFlow<Resource<ClientResponse>> = _updateClientState.asStateFlow()

    // ---------------------------------------------
    // STATE: Obtener un cliente por ID
    // ---------------------------------------------
    private val _oneClientState = MutableStateFlow<Resource<ClientResponse>>(Resource.Loading)
    val oneClientState: StateFlow<Resource<ClientResponse>> = _oneClientState.asStateFlow()

    // ---------------------------------------------
    // STATE: Eliminar cliente
    // ---------------------------------------------
    private val _deleteClientState = mutableStateOf<Resource<DefaultResponse>>(Resource.Idle)
    val deleteClientState: State<Resource<DefaultResponse>> = _deleteClientState

    // ---------------------------------------------
    // STATE: Búsqueda
    // ---------------------------------------------
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        loadClients(fullName = "", email = "", providerId = 1)
    }

    // ---------------------------------------------
    // FUNCIÓN: Cargar lista de clientes
    // ---------------------------------------------
    fun loadClients(
        fullName: String = "",
        email: String = "",
        providerId: Int = 1
    ) {
        viewModelScope.launch {
            reservasUC.getClientPorProviderUC(
                providerId = providerId,
                fullName = fullName,
                email = email
            )
                .onStart {
                    _clientsState.value = Resource.Loading
                }
                .catch { e ->
                    _clientsState.value = Resource.Failure(e.message ?: "Error al cargar clientes")
                }
                .collectLatest { result ->
                    _clientsState.value = result

                    // 🔹 Actualizar lista local cuando llegan datos
                    if (result is Resource.Success) {
                        _localClientsList.value = result.data
                    }
                }
        }
    }

    // ---------------------------------------------
    // FUNCIÓN: Actualizar query de búsqueda
    // ---------------------------------------------
    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    // ---------------------------------------------
    // FUNCIÓN: Obtener cliente por ID
    // ---------------------------------------------
    fun getClientById(clientId: Int) {
        viewModelScope.launch {
            reservasUC.getClientPorIdUC(clientId)
                .onStart {
                    _oneClientState.value = Resource.Loading
                }
                .catch { e ->
                    _oneClientState.value = Resource.Failure(e.message ?: "Error al cargar cliente")
                }
                .collectLatest { result ->
                    _oneClientState.value = result
                }
        }
    }

    // ---------------------------------------------
    // FUNCIÓN: Crear cliente
    // ---------------------------------------------
    fun createClient(request: ClientCreateRequest) {
        viewModelScope.launch {
            reservasUC.createClientUC(request)
                .onStart {
                    _createClientState.value = Resource.Loading
                }
                .catch { e ->
                    _createClientState.value = Resource.Failure(e.message ?: "Error al crear cliente")
                }
                .collectLatest { result ->
                    _createClientState.value = result

                    if (result is Resource.Success) {
                        delay(500)
                        loadClients(fullName = "", email = "", providerId = 1)
                    }
                }
        }
    }

    // ---------------------------------------------
    // FUNCIÓN: Actualizar cliente
    // ---------------------------------------------
    fun updateClient(clientId: Int, request: ClientUpdateRequest) {
        viewModelScope.launch {
            reservasUC.updateClientUC(clientId, request)
                .onStart {
                    _updateClientState.value = Resource.Loading
                }
                .catch { e ->
                    _updateClientState.value = Resource.Failure(e.message ?: "Error al actualizar cliente")
                }
                .collectLatest { result ->
                    _updateClientState.value = result

                    if (result is Resource.Success) {
                        delay(500)
                        loadClients(fullName = "", email = "", providerId = 1)
                    }
                }
        }
    }

    // ---------------------------------------------
    // 🔥 FUNCIÓN: Eliminar cliente - VERSIÓN AGRESIVA
    // ---------------------------------------------
    private val TAG = "DELETE_CLIENT"

    fun deleteClient(clientId: Int) {
        viewModelScope.launch {
            Log.d(TAG, "🧨 Iniciando deleteClient | clientId = $clientId")

            try {
                // 🔹 PASO 1: Estado actual
                val currentList = _localClientsList.value
                Log.d(TAG, "📋 Lista actual size = ${currentList.size}")

                val updatedList = currentList.filter { it.id != clientId }
                Log.d(
                    TAG,
                    "🗑️ Cliente eliminado localmente | before=${currentList.size} after=${updatedList.size}"
                )

                _localClientsList.value = updatedList

                // 🔹 PASO 2: Loading
                _deleteClientState.value = Resource.Loading
                Log.d(TAG, "⏳ Estado delete = Loading")

                // 🔹 PASO 3: Llamada al servidor
                Log.d(TAG, "🌐 Llamando a deleteClientUC...")
                val response = reservasUC.deleteClientUC(clientId)
                Log.d(TAG, "📡 Respuesta recibida: $response")

                // 🔹 PASO 4: Manejo de respuesta
                when (response) {
                    is Resource.Success -> {
                        Log.d(TAG, "✅ DELETE SUCCESS en backend")

                        _deleteClientState.value = Resource.Success(response.data)

                        Log.d(TAG, "🔄 Esperando 1s para sincronizar...")
                        delay(1000)

                        Log.d(TAG, "🔄 Recargando clientes desde backend")
                        loadClients(fullName = "", email = "", providerId = 1)

                        delay(300)
                        _deleteClientState.value = Resource.Idle
                        Log.d(TAG, "♻️ Estado delete = Idle")
                    }

                    is Resource.Failure -> {
                        Log.e(
                            TAG,
                            "❌ DELETE FAILURE | message=${response.message}"
                        )

                        // 🔹 Restaurar lista
                        _localClientsList.value = currentList
                        Log.d(TAG, "↩️ Lista restaurada | size=${currentList.size}")

                        _deleteClientState.value =
                            Resource.Failure(response.message)
                    }

                    else -> {
                        Log.w(TAG, "⚠️ Estado inesperado: $response")
                        _deleteClientState.value = Resource.Idle
                    }
                }

            } catch (e: Exception) {
                Log.e(TAG, "💥 EXCEPCIÓN en deleteClient", e)

                Log.d(TAG, "↩️ Recargando clientes por error")
                loadClients(fullName = "", email = "", providerId = 1)

                _deleteClientState.value = Resource.Failure(
                    e.localizedMessage ?: "Error al eliminar cliente"
                )
            }
        }
    }


    // ---------------------------------------------
    // FUNCIÓN: Resetear estados
    // ---------------------------------------------
    fun resetCreateState() {
        _createClientState.value = Resource.Idle
    }

    fun resetUpdateState() {
        _updateClientState.value = Resource.Idle
    }

    fun resetDeleteState() {
        _deleteClientState.value = Resource.Idle
    }

    fun resetOneClientState() {
        _oneClientState.value = Resource.Loading
    }

    fun clearSearch() {
        _searchQuery.value = ""
    }
}
