package com.optic.pramosreservasappz.presentation.screens.tusventas

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.optic.pramosreservasappz.domain.model.response.DefaultResponse
import com.optic.pramosreservasappz.domain.model.saleitem.SaleItemResponse
import com.optic.pramosreservasappz.domain.model.saleitem.SaleItemUpdateRequest
import com.optic.pramosreservasappz.domain.model.sales.CreateSaleWithItemsRequest
import com.optic.pramosreservasappz.domain.model.sales.SaleCreateRequest
import com.optic.pramosreservasappz.domain.model.saleitem.SaleItemCreateRequest
import com.optic.pramosreservasappz.domain.model.sales.SaleResponse
import com.optic.pramosreservasappz.domain.model.sales.SaleUpdateRequest
import com.optic.pramosreservasappz.domain.model.sales.SaleWithItemsResponse
import com.optic.pramosreservasappz.domain.useCase.reservas.ReservasUC
import com.optic.pramosreservasappz.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistorialViewModel @Inject constructor(
    private val reservasUC: ReservasUC
) : ViewModel() {

    // ---------------------------------------------
    // STATE: Lista de ventas
    // ---------------------------------------------
    private val _salesState =
        MutableStateFlow<Resource<List<SaleResponse>>>(Resource.Loading)
    val salesState: StateFlow<Resource<List<SaleResponse>>> =
        _salesState.asStateFlow()


    // 🔥 Lista local para UI inmediata
    private val _localSalesList =
        MutableStateFlow<List<SaleResponse>>(emptyList())
    val localSalesList: StateFlow<List<SaleResponse>> =
        _localSalesList.asStateFlow()

    // ---------------------------------------------
    // STATE: Crear venta
    // ---------------------------------------------
    private val _createSaleState =
        mutableStateOf<Resource<SaleResponse>>(Resource.Idle)
    val createSaleState: State<Resource<SaleResponse>> =
        _createSaleState

    // ---------------------------------------------
    // STATE: Crear venta con items
    // ---------------------------------------------
    private val _createSaleWithItemsState =
        mutableStateOf<Resource<SaleResponse>>(Resource.Idle)
    val createSaleWithItemsState: State<Resource<SaleResponse>> =
        _createSaleWithItemsState

    // ---------------------------------------------
    // STATE: Obtener venta por ID
    // ---------------------------------------------
    private val _oneSaleState =
        mutableStateOf<Resource<SaleWithItemsResponse>>(Resource.Loading)
    val oneSaleState: State<Resource<SaleWithItemsResponse>> =
        _oneSaleState

    // ---------------------------------------------
    // STATE: Actualizar venta
    // ---------------------------------------------
    private val _updateSaleState =
        mutableStateOf<Resource<SaleResponse>>(Resource.Idle)
    val updateSaleState: State<Resource<SaleResponse>> =
        _updateSaleState

    // ---------------------------------------------
    // STATE: Delete
    // ---------------------------------------------
    private val _deleteSaleState =
        mutableStateOf<Resource<DefaultResponse>>(Resource.Idle)
    val deleteSaleState: State<Resource<DefaultResponse>> =
        _deleteSaleState

    //private val TAG = "DELETE_SALE"

    // ---------------------------------------------
    // STATE: Búsqueda
    // ---------------------------------------------
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // ---------------------------------------------
    // FUNCIÓN: Actualizar query de búsqueda
    // ---------------------------------------------
    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    init {
        loadSales(ownerId = 1)
    }

    // ---------------------------------------------
    // LOAD SALES (Flow ✅)
    // ---------------------------------------------
    fun loadSales(ownerId: Int) {
        viewModelScope.launch {
            reservasUC.getSalesByOwnerUC(ownerId, 50)
                .onStart {
                    _salesState.value = Resource.Loading
                }
                .catch { e ->
                    _salesState.value =
                        Resource.Failure(e.message ?: "Error al cargar ventas")
                }
                .collectLatest { result ->
                    _salesState.value = result

                    if (result is Resource.Success) {
                        _localSalesList.value = result.data
                    }
                }
        }
    }

    // ---------------------------------------------
    // GET BY ID (suspend ✅)
    // ---------------------------------------------
    fun getSaleById(saleId: Int) {
        viewModelScope.launch {
            _oneSaleState.value = Resource.Loading

            try {
                val result = reservasUC.getSaleByIdUC(saleId)
                _oneSaleState.value = result
            } catch (e: Exception) {
                _oneSaleState.value =
                    Resource.Failure(e.message ?: "Error al obtener venta")
            }
        }
    }

    // ---------------------------------------------
    // CREATE SALE
    // ---------------------------------------------
    fun createSale(request: SaleCreateRequest) {
        viewModelScope.launch {
            _createSaleState.value = Resource.Loading

            try {
                val result = reservasUC.createSaleUC(request)
                _createSaleState.value = result

                if (result is Resource.Success) {
                    delay(500)
                    loadSales(ownerId = 1)
                }

            } catch (e: Exception) {
                _createSaleState.value =
                    Resource.Failure(e.message ?: "Error al crear venta")
            }
        }
    }

    // ---------------------------------------------
    // CREATE SALE WITH ITEMS 🔥
    // ---------------------------------------------
    fun createSaleWithItems(request: CreateSaleWithItemsRequest) {
        viewModelScope.launch {
            _createSaleWithItemsState.value = Resource.Loading

            try {
                val result = reservasUC.createSaleWithItemsUC(request)
                _createSaleWithItemsState.value = result

                if (result is Resource.Success) {
                    delay(500)
                    loadSales(ownerId = 1)
                }

            } catch (e: Exception) {
                _createSaleWithItemsState.value =
                    Resource.Failure(e.message ?: "Error al crear venta")
            }
        }
    }

    // ---------------------------------------------
    // UPDATE
    // ---------------------------------------------
    fun updateSale(
        saleId: Int,
        request: SaleUpdateRequest
    ) {
        viewModelScope.launch {
            _updateSaleState.value = Resource.Loading

            try {
                val result = reservasUC.updateSaleUC(saleId, request)
                _updateSaleState.value = result

                if (result is Resource.Success) {
                    delay(500)
                    loadSales(ownerId = 1)
                }

            } catch (e: Exception) {
                _updateSaleState.value =
                    Resource.Failure(e.message ?: "Error al actualizar venta")
            }
        }
    }

    // ---------------------------------------------
    // DELETE SOFT (optimistic 🔥)
    // ---------------------------------------------
    fun deleteSaleSoft(saleId: Int) {
        viewModelScope.launch {
            try {
                val currentList = _localSalesList.value
                val updatedList = currentList.filter { it.id != saleId }

                _localSalesList.value = updatedList
                _deleteSaleState.value = Resource.Loading

                val response = reservasUC.deleteSaleSoftUC(saleId)

                when (response) {
                    is Resource.Success -> {
                        _deleteSaleState.value = response
                        delay(800)
                        loadSales(ownerId = 1)
                        _deleteSaleState.value = Resource.Idle
                    }

                    is Resource.Failure -> {
                        _localSalesList.value = currentList
                        _deleteSaleState.value = response
                    }

                    else -> Unit
                }

            } catch (e: Exception) {
                loadSales(ownerId = 1)
                _deleteSaleState.value =
                    Resource.Failure(e.message ?: "Error al eliminar venta")
            }
        }
    }

    // ---------------------------------------------
    // DELETE HARD
    // ---------------------------------------------
    fun deleteSaleHard(saleId: Int) {
        viewModelScope.launch {
            _deleteSaleState.value = Resource.Loading

            try {
                val result = reservasUC.deleteSaleHardUC(saleId)
                _deleteSaleState.value = result

                if (result is Resource.Success) {
                    delay(500)
                    loadSales(ownerId = 1)
                }

            } catch (e: Exception) {
                _deleteSaleState.value =
                    Resource.Failure(e.message ?: "Error al eliminar venta")
            }
        }
    }

    // ---------------------------------------------
    // RESET STATES
    // ---------------------------------------------
    fun resetCreateSaleState() {
        _createSaleState.value = Resource.Idle
    }

    fun resetCreateSaleWithItemsState() {
        _createSaleWithItemsState.value = Resource.Idle
    }

    fun resetUpdateSaleState() {
        _updateSaleState.value = Resource.Idle
    }

    fun resetDeleteSaleState() {
        _deleteSaleState.value = Resource.Idle
    }

    fun resetOneSaleState() {
        _oneSaleState.value = Resource.Loading
    }



    // ---------------------------------------------
    // STATE: Lista de items por venta
    // ---------------------------------------------
    private val _itemsState =
        MutableStateFlow<Resource<List<SaleItemResponse>>>(Resource.Loading)
    val itemsState: StateFlow<Resource<List<SaleItemResponse>>> =
        _itemsState.asStateFlow()

    // 🔥 lista local (optimistic UI)
    private val _localItemsList =
        MutableStateFlow<List<SaleItemResponse>>(emptyList())
    val localItemsList: StateFlow<List<SaleItemResponse>> =
        _localItemsList.asStateFlow()

    // ---------------------------------------------
    // STATE: Crear item
    // ---------------------------------------------
    private val _createItemState =
        mutableStateOf<Resource<SaleItemResponse>>(Resource.Idle)
    val createItemState: State<Resource<SaleItemResponse>> =
        _createItemState

    // ---------------------------------------------
    // STATE: Bulk create
    // ---------------------------------------------
    private val _createBulkState =
        mutableStateOf<Resource<List<SaleItemResponse>>>(Resource.Idle)
    val createBulkState: State<Resource<List<SaleItemResponse>>> =
        _createBulkState

    // ---------------------------------------------
    // STATE: Obtener item por ID
    // ---------------------------------------------
    private val _oneItemState =
        mutableStateOf<Resource<SaleItemResponse>>(Resource.Loading)
    val oneItemState: State<Resource<SaleItemResponse>> =
        _oneItemState

    // ---------------------------------------------
    // STATE: Update
    // ---------------------------------------------
    private val _updateItemState =
        mutableStateOf<Resource<SaleItemResponse>>(Resource.Idle)
    val updateItemState: State<Resource<SaleItemResponse>> =
        _updateItemState

    // ---------------------------------------------
    // STATE: Delete
    // ---------------------------------------------
    private val _deleteItemState =
        mutableStateOf<Resource<DefaultResponse>>(Resource.Idle)
    val deleteItemState: State<Resource<DefaultResponse>> =
        _deleteItemState

    private val TAG = "DELETE_ITEM"

    // ---------------------------------------------
    // LOAD ITEMS BY SALE (Flow 🔥)
    // ---------------------------------------------
    fun loadItemsBySale(saleId: Int) {
        viewModelScope.launch {
            reservasUC.getItemsBySaleUC(saleId)
                .onStart {
                    _itemsState.value = Resource.Loading
                }
                .catch { e ->
                    _itemsState.value =
                        Resource.Failure(e.message ?: "Error al cargar items")
                }
                .collectLatest { result ->
                    _itemsState.value = result

                    if (result is Resource.Success) {
                        _localItemsList.value = result.data
                    }
                }
        }
    }

    // ---------------------------------------------
    // GET ITEM BY ID
    // ---------------------------------------------
    fun getItemById(itemId: Int) {
        viewModelScope.launch {
            _oneItemState.value = Resource.Loading

            try {
                val result = reservasUC.getSaleItemByIdUC(itemId)
                _oneItemState.value = result
            } catch (e: Exception) {
                _oneItemState.value =
                    Resource.Failure(e.message ?: "Error al obtener item")
            }
        }
    }

    // ---------------------------------------------
    // CREATE ITEM
    // ---------------------------------------------
    fun createItem(request: SaleItemCreateRequest, saleId: Int) {
        viewModelScope.launch {
            _createItemState.value = Resource.Loading

            try {
                val result = reservasUC.createSaleItemUC(request)
                _createItemState.value = result

                if (result is Resource.Success) {
                    delay(300)
                    loadItemsBySale(saleId)
                }

            } catch (e: Exception) {
                _createItemState.value =
                    Resource.Failure(e.message ?: "Error al crear item")
            }
        }
    }

    // ---------------------------------------------
    // BULK CREATE 🔥
    // ---------------------------------------------
    fun createItemsBulk(
        request: List<SaleItemCreateRequest>,
        saleId: Int
    ) {
        viewModelScope.launch {
            _createBulkState.value = Resource.Loading

            try {
                val result = reservasUC.createSaleItemBulkUC(request)
                _createBulkState.value = result

                if (result is Resource.Success) {
                    delay(300)
                    loadItemsBySale(saleId)
                }

            } catch (e: Exception) {
                _createBulkState.value =
                    Resource.Failure(e.message ?: "Error al crear items")
            }
        }
    }

    // ---------------------------------------------
    // UPDATE ITEM
    // ---------------------------------------------
    fun updateItem(
        itemId: Int,
        request: SaleItemUpdateRequest,
        saleId: Int
    ) {
        viewModelScope.launch {
            _updateItemState.value = Resource.Loading

            try {
                val result = reservasUC.updateSaleItemUC(itemId, request)
                _updateItemState.value = result

                if (result is Resource.Success) {
                    delay(300)
                    loadItemsBySale(saleId)
                }

            } catch (e: Exception) {
                _updateItemState.value =
                    Resource.Failure(e.message ?: "Error al actualizar item")
            }
        }
    }


}