package com.optic.pramosreservasappz.presentation.screens.newsale

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.optic.pramosreservasappz.domain.model.clients.ClientResponse
import com.optic.pramosreservasappz.domain.model.product.ProductCreateRequest
import com.optic.pramosreservasappz.domain.model.product.ProductResponse
import com.optic.pramosreservasappz.domain.model.product.ProductUpdateRequest
import com.optic.pramosreservasappz.domain.model.product.ProductViewType
import com.optic.pramosreservasappz.domain.model.response.DefaultResponse
import com.optic.pramosreservasappz.domain.model.saleitem.SaleItemResponse
import com.optic.pramosreservasappz.domain.model.sales.CreateSaleWithItemsRequest
import com.optic.pramosreservasappz.domain.model.sales.SaleCreateRequest
import com.optic.pramosreservasappz.domain.model.saleitem.SaleItemCreateRequest
import com.optic.pramosreservasappz.domain.model.sales.SaleResponse
import com.optic.pramosreservasappz.domain.model.sales.SaleWithItemsResponse
import com.optic.pramosreservasappz.domain.model.sales.types.SaleType
import com.optic.pramosreservasappz.domain.useCase.reservas.ReservasUC
import com.optic.pramosreservasappz.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewSaleViewModel @Inject constructor(
    private val reservasUC: ReservasUC
) : ViewModel() {

    private var businessId: Int? = null

    fun setBusinessId(value: Int) {
        businessId = value
    }


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


    // VENTAS RECIETES ACORDE A PARAMETRO

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


            } catch (e: Exception) {
                _createSaleWithItemsState.value =
                    Resource.Failure(e.message ?: "Error al crear venta")
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

            } catch (e: Exception) {
                _createBulkState.value =
                    Resource.Failure(e.message ?: "Error al crear items")
            }
        }
    }




    // ---------------------------------------------
    // RESET STATES
    // ---------------------------------------------
    fun resetCreateItemState() {
        _createItemState.value = Resource.Idle
    }

    fun resetCreateBulkState() {
        _createBulkState.value = Resource.Idle
    }




    // ---------------------------------------------
    // STATE: Lista de productos
    // ---------------------------------------------
// LISTAS → Flow
    private val _productsState = MutableStateFlow<Resource<List<ProductResponse>>>(Resource.Loading)
    val productsState: StateFlow<Resource<List<ProductResponse>>> = _productsState.asStateFlow()

    private val _localProductsList = MutableStateFlow<List<ProductResponse>>(emptyList())
    val localProductsList: StateFlow<List<ProductResponse>> = _localProductsList.asStateFlow()

    // ONE SHOT → Compose State
    private val _createProductState = mutableStateOf<Resource<ProductResponse>>(Resource.Idle)
    val createProductState: State<Resource<ProductResponse>> = _createProductState

    private val _updateProductState = mutableStateOf<Resource<ProductResponse>>(Resource.Idle)
    val updateProductState: State<Resource<ProductResponse>> = _updateProductState

    private val _oneProductState = mutableStateOf<Resource<ProductResponse>>(Resource.Loading)
    val oneProductState: State<Resource<ProductResponse>> = _oneProductState

    private val _deleteProductState = mutableStateOf<Resource<DefaultResponse>>(Resource.Idle)
    val deleteProductState: State<Resource<DefaultResponse>> = _deleteProductState

    // ---------------------------------------------
    // STATE: Search
    // ---------------------------------------------
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()



    // ---------------------------------------------
    // LOAD PRODUCTS
    // ---------------------------------------------
    fun loadProducts(
        businessId: Int,
        name: String = ""
    ) {
        viewModelScope.launch {
            reservasUC.getProductsByBusinessUC(businessId, name)
                .onStart {
                    _productsState.value = Resource.Loading
                }
                .catch { e ->
                    _productsState.value = Resource.Failure(e.message ?: "Error al cargar productos")
                }
                .collectLatest { result ->
                    _productsState.value = result

                    if (result is Resource.Success) {
                        _localProductsList.value = result.data
                    }
                }
        }
    }

    // ---------------------------------------------
    // SEARCH
    // ---------------------------------------------
    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun clearSearch() {
        _searchQuery.value = ""
    }

    // ---------------------------------------------
    // GET BY ID
    // ---------------------------------------------
    fun getProductById(productId: Int) {
        viewModelScope.launch {
            _oneProductState.value = Resource.Loading

            when (val result = reservasUC.getProductByIdUC(productId)) {
                is Resource.Success -> {
                    _oneProductState.value = result
                }
                is Resource.Failure -> {
                    _oneProductState.value = result
                }
                else -> {}
            }
        }
    }

    // ---------------------------------------------
    // CREATE PRODUCT
    // ---------------------------------------------
    fun createProduct(request: ProductCreateRequest) {
        viewModelScope.launch {
            _createProductState.value = Resource.Loading

            when (val result = reservasUC.createProductUC(request)) {
                is Resource.Success -> {
                    _createProductState.value = result
                    delay(500)
                    (request.businessId?:null)?.let { loadProducts(businessId = it) }
                }
                is Resource.Failure -> {
                    _createProductState.value = result
                }
                else -> {}
            }
        }
    }


    private val _productViewType = MutableStateFlow(ProductViewType.GRID)
    val productViewType: StateFlow<ProductViewType> = _productViewType

    fun updateProductViewType(type: ProductViewType) {
        _productViewType.value = type
    }



    // ---------------------------------------------
    // UPDATE PRODUCT
    // ---------------------------------------------
    fun updateProduct(
        productId: Int,
        request: ProductUpdateRequest,
        businessId: Int = 1
    ) {
        viewModelScope.launch {
            _updateProductState.value = Resource.Loading

            when (val result = reservasUC.updateProductUC(productId, request)) {
                is Resource.Success -> {
                    _updateProductState.value = result
                    delay(500)
                    loadProducts(businessId=businessId)
                }
                is Resource.Failure -> {
                    _updateProductState.value = result
                }
                else -> {}
            }
        }
    }

    // ---------------------------------------------
    // DELETE PRODUCT (OPTIMISTA 🔥)
    // ---------------------------------------------

    fun deleteProduct(productId: Int, businessId: Int = 1) {
        viewModelScope.launch {

            try {
                val currentList = _localProductsList.value
                val updatedList = currentList.filter { it.id != productId }

                _localProductsList.value = updatedList
                _deleteProductState.value = Resource.Loading

                val response = reservasUC.deleteProductSoftUC(productId)

                when (response) {
                    is Resource.Success -> {
                        _deleteProductState.value = response
                        delay(800)
                        loadProducts(businessId=businessId)
                        delay(300)
                        _deleteProductState.value = Resource.Idle
                    }

                    is Resource.Failure -> {
                        _localProductsList.value = currentList
                        _deleteProductState.value = response
                    }

                    else -> {
                        _deleteProductState.value = Resource.Idle
                    }
                }

            } catch (e: Exception) {
                _deleteProductState.value =
                    Resource.Failure(e.message ?: "Error al eliminar producto")

                loadProducts(businessId=businessId)
            }
        }
    }

    // ---------------------------------------------
    // RESET STATES
    // ---------------------------------------------
    fun resetCreateState() {
        _createProductState.value = Resource.Idle
    }

    fun resetUpdateState() {
        _updateProductState.value = Resource.Idle
    }

    fun resetDeleteState() {
        _deleteProductState.value = Resource.Idle
    }

    fun resetOneProductState() {
        _oneProductState.value = Resource.Loading
    }


    // ---------------------------------------------
    // STATE: Selected Products (🔥 CARRITO GLOBAL) EN VENTA RAPIDA
    // ---------------------------------------------
    private val _selectedProducts =
        MutableStateFlow<List<Pair<ProductResponse, Int>>>(emptyList())
    val selectedProducts: StateFlow<List<Pair<ProductResponse, Int>>> =
        _selectedProducts.asStateFlow()


    fun addProduct(product: ProductResponse) {

        val currentList = _selectedProducts.value.toMutableList()

        val index = currentList.indexOfFirst { it.first.id == product.id }

        if (index >= 0) {
            val current = currentList[index]
            currentList[index] = current.copy(second = current.second + 1)
        } else {
            currentList.add(product to 1)
        }

        _selectedProducts.value = currentList
    }

    fun removeProduct(product: ProductResponse) {

        val currentList = _selectedProducts.value.toMutableList()

        val index = currentList.indexOfFirst { it.first.id == product.id }

        if (index >= 0) {
            val current = currentList[index]

            if (current.second <= 1) {
                currentList.removeAt(index)
            } else {
                currentList[index] = current.copy(second = current.second - 1)
            }
        }

        _selectedProducts.value = currentList
    }

    fun clearSelectedProducts() {
        _selectedProducts.value = emptyList()
    }

    fun removeAllOfProduct(product: ProductResponse) {
        _selectedProducts.value =
            _selectedProducts.value.filterNot { it.first.id == product.id }
    }

    fun removeProductCompletely(productId: Int) {
        _selectedProducts.update { list ->
            list.filterNot { it.first.id == productId }
        }
    }

    // 🔥 Estado global de animación item vuela al carrito

    data class FlyAnimationData(
        val product: ProductResponse,
        val startX: Float,
        val startY: Float
    )

    private val _flyAnimation = MutableStateFlow<FlyAnimationData?>(null)
    val flyAnimation: StateFlow<FlyAnimationData?> = _flyAnimation

    fun triggerFlyAnimation(data: FlyAnimationData) {
        _flyAnimation.value = data
    }

    fun clearFlyAnimation() {
        _flyAnimation.value = null
    }


    val total: StateFlow<Double> = selectedProducts
        .map { list ->
            list.sumOf { it.first.price * it.second }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            0.0
        )

    val totalItems: StateFlow<Int> = selectedProducts
        .map { list ->
            list.sumOf { it.second } // 🔥 mejor que size (más correcto)
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            0
        )


    // 🔥 STEP 1 STATE
    var saleName by mutableStateOf("")
        private set

    var paymentMethod by mutableStateOf<String?>(null)
        private set

    fun onSaleNameChange(value: String) {
        saleName = value
    }

    fun onPaymentMethodSelected(method: String) {
        paymentMethod = method
    }

    fun resetStepOneState() {
        saleName = ""
        paymentMethod = null
    }


    // ---------------------------------------------
    // STATE: Lista de clientes
    // ---------------------------------------------
    private val _clientsState = MutableStateFlow<Resource<List<ClientResponse>>>(Resource.Loading)
    val clientsState: StateFlow<Resource<List<ClientResponse>>> = _clientsState.asStateFlow()

    // 🔹 NUEVO: Lista mutable para control local
    private val _localClientsList = MutableStateFlow<List<ClientResponse>>(emptyList())
    val localClientsList: StateFlow<List<ClientResponse>> = _localClientsList.asStateFlow()

    // ---------------------------------------------
    // FUNCIÓN: Cargar lista de clientes
    // ---------------------------------------------
    fun loadClients(
        fullName: String = "",
        email: String = "",
        businessId: Int = 1
    ) {
        viewModelScope.launch {
            reservasUC.getClientPorBusinessUC(
                businessId = businessId,
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


    // 🔥 CLIENTE SELECCIONADO
    var selectedClientId by mutableStateOf<Int?>(null)
        private set

    var selectedClientName by mutableStateOf<String?>(null)
        private set

    var selectedClientEmail by mutableStateOf<String?>(null)
        private set

    var selectedClientPhone by mutableStateOf<String?>(null)
        private set

    // 🔥 FUNCIÓN
    fun selectClient(client: ClientResponse) {
        selectedClientId = client.id
        selectedClientName = client.fullName
        selectedClientEmail = client.email
        selectedClientPhone = client.phone
    }


    // ---------------------------------------------
    // STATE: Tipo de flujo de venta
    // RAPID | COMPLETE
    // ---------------------------------------------
    var saleFlowType by mutableStateOf<SaleType?>(null)
        private set

    fun updateSaleFlowType(type: SaleType) {
        saleFlowType = type
    }

    fun clearSaleFlowType() {
        saleFlowType = null
    }

    fun isRapidSale(): Boolean {
        return saleFlowType == SaleType.RAPID
    }

    fun isCompleteSale(): Boolean {
        return saleFlowType == SaleType.COMPLETE
    }
}