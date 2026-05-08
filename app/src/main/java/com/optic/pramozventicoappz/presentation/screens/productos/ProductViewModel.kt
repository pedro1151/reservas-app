package com.optic.pramozventicoappz.presentation.screens.productos

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.optic.pramozventicoappz.domain.model.response.DefaultResponse
import com.optic.pramozventicoappz.domain.useCase.reservas.ReservasUC
import com.optic.pramozventicoappz.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.optic.pramozventicoappz.domain.model.product.MiniProductResponse
import com.optic.pramozventicoappz.domain.model.product.ProductCreateRequest
import com.optic.pramozventicoappz.domain.model.product.ProductResponse
import com.optic.pramozventicoappz.domain.model.product.ProductUpdateRequest
import com.optic.pramozventicoappz.domain.model.product.ProductViewType
import com.optic.pramozventicoappz.presentation.screens.productos.abmproducto.ProductCreateState

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val reservasUC: ReservasUC
) : ViewModel() {

    // ---------------------------------------------
    // STATE: Lista de productos
    // ---------------------------------------------
// LISTAS → Flow
    private val _productsState = MutableStateFlow<Resource<List<MiniProductResponse>>>(Resource.Loading)
    val productsState: StateFlow<Resource<List<MiniProductResponse>>> = _productsState.asStateFlow()

    private val _localProductsList = MutableStateFlow<List<MiniProductResponse>>(emptyList())
    val localProductsList: StateFlow<List<MiniProductResponse>> = _localProductsList.asStateFlow()

    // ONE SHOT → Compose State
    private val _createProductState = mutableStateOf<Resource<MiniProductResponse>>(Resource.Idle)
    val createProductState: State<Resource<MiniProductResponse>> = _createProductState

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
                    (request.businessId ?: null)?.let { loadProducts(businessId = it) }
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
                    loadProducts(businessId = ownerId)
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
                        loadProducts(businessId = ownerId)
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

                loadProducts(businessId = ownerId)
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

    private val _productViewType = MutableStateFlow(ProductViewType.GRID)
    val productViewType: StateFlow<ProductViewType> = _productViewType

    fun updateProductViewType(type: ProductViewType) {
        _productViewType.value = type
    }



    private val _formState = MutableStateFlow(ProductCreateState())
    val formState: StateFlow<ProductCreateState> = _formState

    fun onFormChange(state: ProductCreateState) {
        _formState.value = state
    }

    fun resetForm() {
        _formState.value = ProductCreateState()
    }

}
