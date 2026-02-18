package com.optic.pramosreservasappz.presentation.screens.services

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.optic.pramosreservasappz.domain.model.reservas.services.ServiceCreateRequest
import com.optic.pramosreservasappz.domain.model.reservas.services.ServiceResponse
import com.optic.pramosreservasappz.domain.model.reservas.services.ServiceUpdateRequest
import com.optic.pramosreservasappz.domain.model.response.DefaultResponse
import com.optic.pramosreservasappz.domain.useCase.reservas.ReservasUC
import com.optic.pramosreservasappz.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.util.Log

@HiltViewModel
class ServiceViewModel @Inject constructor(
    private val reservasUC: ReservasUC
) : ViewModel() {

    // ---------------------------------------------
    // STATE: Lista completa o filtrada según búsqueda
    // ---------------------------------------------
    private val _servicesState = MutableStateFlow<Resource<List<ServiceResponse>>>(Resource.Loading)
    val servicesState: StateFlow<Resource<List<ServiceResponse>>> = _servicesState.asStateFlow()

    // 🔹 Lista mutable para control local
    private val _localServicesList = MutableStateFlow<List<ServiceResponse>>(emptyList())
    val localServicesList: StateFlow<List<ServiceResponse>> = _localServicesList.asStateFlow()

    // ---------------------------------------------
    // create service state
    // ---------------------------------------------
    private val _createServiceState = MutableStateFlow<Resource<ServiceResponse>>(Resource.Idle)
    val createServiceState: StateFlow<Resource<ServiceResponse>> = _createServiceState.asStateFlow()

    // ---------------------------------------------
    // update service state
    // ---------------------------------------------
    private val _updateServiceState = MutableStateFlow<Resource<ServiceResponse>>(Resource.Idle)
    val updateServiceState: StateFlow<Resource<ServiceResponse>> = _updateServiceState.asStateFlow()

    // ---------------------------------------------
    // STATE: servicio por id state
    // ---------------------------------------------
    private val _serviceState = MutableStateFlow<Resource<ServiceResponse>>(Resource.Loading)
    val serviceState: StateFlow<Resource<ServiceResponse>> = _serviceState.asStateFlow()

    // ---------------------------------------------
    // STATE: Eliminar servicio
    // ---------------------------------------------
    private val _deleteServiceState = mutableStateOf<Resource<DefaultResponse>>(Resource.Idle)
    val deleteServiceState: State<Resource<DefaultResponse>> = _deleteServiceState

    // ---------------------------------------------
    // QUERY en StateFlow
    // ---------------------------------------------
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        getServicesByProvider(1, "")
    }

    // ---------------------------------------------
    // FUNCIÓN: Actualizar query de búsqueda
    // ---------------------------------------------
    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    // ---------------------------------------------
    // GET SERVICES POR PROVIDER
    // ---------------------------------------------
    fun getServicesByProvider(
        providerId: Int,
        name: String = ""
    ) {
        viewModelScope.launch {
            reservasUC.getServicesPorProviderUC(providerId, name)
                .onStart {
                    _servicesState.value = Resource.Loading
                }
                .catch { e ->
                    _servicesState.value = Resource.Failure(e.message ?: "Error al cargar servicios")
                }
                .collectLatest { result ->
                    _servicesState.value = result

                    // 🔹 Actualizar lista local cuando llegan datos
                    if (result is Resource.Success) {
                        _localServicesList.value = result.data
                    }
                }
        }
    }

    // ---------------------------------------------
    // CREATE SERVICE
    // ---------------------------------------------
    fun createService(
        request: ServiceCreateRequest
    ) {
        viewModelScope.launch {
            reservasUC.createServiceUC(request)
                .onStart {
                    _createServiceState.value = Resource.Loading
                }
                .catch { e ->
                    _createServiceState.value = Resource.Failure(e.message ?: "Error al crear servicio")
                }
                .collectLatest { result ->
                    _createServiceState.value = result

                    if (result is Resource.Success) {
                        delay(500)
                        getServicesByProvider(1)
                    }
                }
        }
    }

    // ---------------------------------------------
    // UPDATE SERVICE
    // ---------------------------------------------
    fun updateService(
        serviceId: Int,
        request: ServiceUpdateRequest
    ) {
        viewModelScope.launch {
            try {
                Log.d("UPDATE_SERVICE", "➡️ Iniciando updateService")
                Log.d("UPDATE_SERVICE", "serviceId: $serviceId")
                Log.d("UPDATE_SERVICE", "request: $request")

                reservasUC.updateServiceUC(serviceId, request)
                    .onStart {
                        Log.d("UPDATE_SERVICE", "⏳ Loading...")
                        _updateServiceState.value = Resource.Loading
                    }
                    .catch { e ->
                        Log.e("UPDATE_SERVICE", "❌ Error en flow", e)
                        _updateServiceState.value = Resource.Failure(e.message ?: "Error al actualizar servicio")
                    }
                    .collectLatest { result ->
                        when (result) {
                            is Resource.Loading -> {
                                Log.d("UPDATE_SERVICE", "⏳ Loading...")
                            }
                            is Resource.Success -> {
                                Log.d("UPDATE_SERVICE", "✅ Success: ${result.data}")
                                _updateServiceState.value = result
                                delay(500)
                                getServicesByProvider(1)
                            }
                            is Resource.Failure -> {
                                Log.e("UPDATE_SERVICE", "❌ Failure: ${result.message}")
                                _updateServiceState.value = result
                            }
                            else -> {
                                Log.w("UPDATE_SERVICE", "⚠️ Estado desconocido: $result")
                            }
                        }
                    }

            } catch (e: Exception) {
                Log.e("UPDATE_SERVICE", "🔥 Excepción no controlada en updateService", e)
                _updateServiceState.value = Resource.Failure(
                    message = e.message ?: "Error inesperado al actualizar servicio"
                )
            }
        }
    }

    // ---------------------------------------------
    // GET SERVICE POR ID
    // ---------------------------------------------
    fun getServiceById(serviceId: Int) {
        viewModelScope.launch {
            Log.d("ServiceVM", "🟢 getServiceById() llamado con id = $serviceId")

            reservasUC.getServicesPorIdUC(serviceId)
                .onStart {
                    Log.d("ServiceVM", "🔄 Flow iniciado getServicesPorIdUC")
                    _serviceState.value = Resource.Loading
                }
                .catch { e ->
                    Log.e("ServiceVM", "❌ Error en Flow getServiceById", e)
                    _serviceState.value = Resource.Failure(e.message ?: "Error desconocido")
                }
                .collectLatest { result ->
                    Log.d("ServiceVM", "📥 Resultado recibido: $result")
                    _serviceState.value = result
                }
        }
    }

    // ---------------------------------------------
    // 🔥 FUNCIÓN: Eliminar servicio - VERSIÓN CORREGIDA
    // ---------------------------------------------
    private val TAG = "DELETE_SERVICE"

    fun deleteService(serviceId: Int) {
        viewModelScope.launch {
            Log.d(TAG, "🧨 Iniciando deleteService | serviceId = $serviceId")

            try {
                // 🔹 PASO 1: Estado actual
                val currentList = _localServicesList.value
                Log.d(TAG, "📋 Lista actual size = ${currentList.size}")

                val updatedList = currentList.filter { it.id != serviceId }
                Log.d(
                    TAG,
                    "🗑️ Servicio eliminado localmente | before=${currentList.size} after=${updatedList.size}"
                )

                _localServicesList.value = updatedList

                // 🔹 PASO 2: Loading
               // _deleteServiceState.value = Resource.Loading
                Log.d(TAG, "⏳ Estado delete = Loading")

                // 🔹 PASO 3: Llamada al servidor
                Log.d(TAG, "🌐 Llamando a deleteServiceUC...")
                //val response = reservasUC.deleteServiceUC(serviceId)
               // Log.d(TAG, "📡 Respuesta recibida: $response")

                // 🔹 PASO 4: Manejo de respuesta
                /*
                when (response) {

                    is Resource.Success -> {
                        Log.d(TAG, "✅ DELETE SUCCESS en backend")

                        _deleteServiceState.value = Resource.Success(response.data)

                        Log.d(TAG, "🔄 Esperando 1s para sincronizar...")
                        delay(1000)

                        Log.d(TAG, "🔄 Recargando servicios desde backend")
                        getServicesByProvider(1)

                        delay(300)
                        _deleteServiceState.value = Resource.Idle
                        Log.d(TAG, "♻️ Estado delete = Idle")
                    }

                    is Resource.Failure -> {
                        Log.e(
                            TAG,
                            "❌ DELETE FAILURE | message=${response.message}"
                        )

                        // 🔹 Restaurar lista
                        _localServicesList.value = currentList
                        Log.d(TAG, "↩️ Lista restaurada | size=${currentList.size}")

                        _deleteServiceState.value =
                            Resource.Failure(response.message)
                    }

                    else -> {
                        Log.w(TAG, "⚠️ Estado inesperado: $response")
                        _deleteServiceState.value = Resource.Idle
                    }
                }
            */
            } catch (e: Exception) {
                Log.e(TAG, "💥 EXCEPCIÓN en deleteService", e)

                Log.d(TAG, "↩️ Recargando servicios por error")
                getServicesByProvider(1)

                _deleteServiceState.value = Resource.Failure(
                    e.localizedMessage ?: "Error al eliminar servicio"
                )
            }
        }
    }

    // ---------------------------------------------
    // FUNCIÓN: Resetear estados
    // ---------------------------------------------
    fun resetCreateState() {
        _createServiceState.value = Resource.Idle
    }

    fun resetUpdateState() {
        _updateServiceState.value = Resource.Idle
    }

    fun resetDeleteState() {
        _deleteServiceState.value = Resource.Idle
    }

    fun resetServiceState() {
        _serviceState.value = Resource.Loading
    }

    fun clearSearch() {
        _searchQuery.value = ""
    }
}
