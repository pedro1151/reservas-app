package com.optic.pramosreservasappz.presentation.screens.productos

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.optic.pramosreservasappz.domain.model.services.ServiceCreateRequest
import com.optic.pramosreservasappz.domain.model.services.ServiceResponse
import com.optic.pramosreservasappz.domain.model.services.ServiceUpdateRequest
import com.optic.pramosreservasappz.domain.model.response.DefaultResponse
import com.optic.pramosreservasappz.domain.useCase.reservas.ReservasUC
import com.optic.pramosreservasappz.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.util.Log
import com.optic.pramosreservasappz.domain.model.product.ProductCreateRequest
import com.optic.pramosreservasappz.domain.model.product.ProductResponse
import com.optic.pramosreservasappz.domain.model.product.ProductUpdateRequest
import com.optic.pramosreservasappz.presentation.screens.services.abmservicio.ServiceCreateState

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val reservasUC: ReservasUC
) : ViewModel() {

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
    // INIT
    // ---------------------------------------------
    init {
        loadProducts(ownerId = 1, name = "")
    }

    // ---------------------------------------------
    // LOAD PRODUCTS
    // ---------------------------------------------
    fun loadProducts(
        ownerId: Int,
        name: String = ""
    ) {
        viewModelScope.launch {
            reservasUC.getProductsByUserUC(ownerId, name)
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
                    loadProducts(ownerId = request.userId ?: 1)
                }
                is Resource.Failure -> {
                    _createProductState.value = result
                }
                else -> {}
            }
        }
    }

    // ---------------------------------------------
    // CREATE SAFE (opcional)
    // ---------------------------------------------
    fun createProductSafe(request: ProductCreateRequest) {
        viewModelScope.launch {
            _createProductState.value = Resource.Loading

            when (val result = reservasUC.createProductSafeUC(request)) {
                is Resource.Success -> {
                    _createProductState.value = result
                    delay(500)
                    loadProducts(ownerId = request.userId ?: 1)
                }
                is Resource.Failure -> {
                    _createProductState.value = result
                }
                else -> {}
            }
        }
    }

    // ---------------------------------------------
    // UPDATE PRODUCT
    // ---------------------------------------------
    fun updateProduct(
        productId: Int,
        request: ProductUpdateRequest,
        ownerId: Int = 1
    ) {
        viewModelScope.launch {
            _updateProductState.value = Resource.Loading

            when (val result = reservasUC.updateProductUC(productId, request)) {
                is Resource.Success -> {
                    _updateProductState.value = result
                    delay(500)
                    loadProducts(ownerId = ownerId)
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

    fun deleteProduct(productId: Int, ownerId: Int = 1) {
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
                        loadProducts(ownerId = ownerId)
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

                loadProducts(ownerId = ownerId)
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
}
