package com.optic.pramosreservasappz.presentation.screens.services

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.optic.pramosreservasappz.domain.model.reservas.services.ServiceCreateRequest
import com.optic.pramosreservasappz.domain.model.reservas.services.ServiceResponse
import com.optic.pramosreservasappz.domain.model.reservas.services.ServiceUpdateRequest
import com.optic.pramosreservasappz.domain.repository.ReservasRepository
import com.optic.pramosreservasappz.domain.useCase.reservas.ReservasUC
import com.optic.pramosreservasappz.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
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
    val servicesState: StateFlow<Resource<List<ServiceResponse>>> = _servicesState


    // ---------------------------------------------
    // create service state
    // ---------------------------------------------
    private val _createServiceState = MutableStateFlow<Resource<ServiceResponse>>(Resource.Idle)
    val createServiceState: StateFlow<Resource<ServiceResponse>> = _createServiceState


    // ---------------------------------------------
    // update service state
    // ---------------------------------------------
    private val _updateServiceState = MutableStateFlow<Resource<ServiceResponse>>(Resource.Idle)
    val updateServiceState: StateFlow<Resource<ServiceResponse>> = _updateServiceState


    // ---------------------------------------------
    // STATE: servicio por id state
    // ---------------------------------------------
    private val _serviceState = MutableStateFlow<Resource<ServiceResponse>>(Resource.Loading)
    val serviceState: StateFlow<Resource<ServiceResponse>> = _serviceState


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
                        getServicesByProvider(1, "")
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
    // GET SERVICES POR PROVIDER
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


    // ---------------------------------------------
    // CREATE SERVICE
    // ---------------------------------------------
    fun createService(
        request: ServiceCreateRequest
    ) {
        viewModelScope.launch {
            reservasUC.createServiceUC(request)
                .collectLatest { result ->
                    _createServiceState.value = result
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
                    .collectLatest { result ->
                        when (result) {
                            is Resource.Loading -> {
                                Log.d("UPDATE_SERVICE", "⏳ Loading...")
                            }
                            is Resource.Success -> {
                                Log.d("UPDATE_SERVICE", "✅ Success: ${result.data}")
                            }
                            is Resource.Failure -> {
                                Log.e(
                                    "UPDATE_SERVICE",
                                    "❌ Failure: ${result.message}",
                                   // result.exception // si tu Resource.Failure tiene exception
                                )
                            }
                            else -> {
                                Log.w("UPDATE_SERVICE", "⚠️ Estado desconocido: $result")
                            }
                        }
                        _updateServiceState.value = result
                    }

            } catch (e: Exception) {
                Log.e(
                    "UPDATE_SERVICE",
                    "🔥 Excepción no controlada en updateService",
                    e // ✅ esto imprime todo el stack trace en Logcat
                )
                _updateServiceState.value = Resource.Failure(
                    message = e.message ?: "Error inesperado al actualizar servicio",
                    //exception = e
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


}
