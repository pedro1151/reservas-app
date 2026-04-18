package com.optic.pramosreservasappz.presentation.screens.business




import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.optic.pramosreservasappz.domain.useCase.reservas.ReservasUC
import com.optic.pramosreservasappz.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.optic.pramosreservasappz.domain.model.auth.BasicUserResponse
import com.optic.pramosreservasappz.domain.model.business.colaboradores.UserCollabCreateRequest
import com.optic.pramosreservasappz.domain.model.business.colaboradores.UserMemberResponse
import com.optic.pramosreservasappz.domain.model.business.completebusiness.BusinessCompleteResponse
import com.optic.pramosreservasappz.domain.useCase.auth.AuthUseCase

@HiltViewModel
class BusinessViewModel @Inject constructor(
    private val reservasUC: ReservasUC,
    private  val authUseCase: AuthUseCase
) : ViewModel() {

    // ---------------------------------------------
    // STATE: Lista de productos
    // ---------------------------------------------
// LISTAS → Flow
    private val _membersState = MutableStateFlow<Resource<List<UserMemberResponse>>>(Resource.Loading)
    val membersState: StateFlow<Resource<List<UserMemberResponse>>> = _membersState.asStateFlow()

    private val _localMembersList = MutableStateFlow<List<UserMemberResponse>>(emptyList())
    val localMembersList: StateFlow<List<UserMemberResponse>> = _localMembersList.asStateFlow()

    // ONE SHOT → Compose State
    private val _createMemberState = mutableStateOf<Resource<BasicUserResponse>>(Resource.Idle)
    val createMemberState: State<Resource<BasicUserResponse>> = _createMemberState


    private val _businessState = mutableStateOf<Resource<BusinessCompleteResponse>>(Resource.Idle)
    val businessState: State<Resource<BusinessCompleteResponse>> = _businessState

    // ---------------------------------------------
    // STATE: Search
    // ---------------------------------------------
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    //


    // ---------------------------------------------
    // LOAD PRODUCTS
    // ---------------------------------------------
    fun loadMembers(
        businessId: Int,
        email: String = "",
        username: String = ""

    ) {
        viewModelScope.launch {
            authUseCase.getBusinessMembersUC(
                businessId = businessId,
                email =  email,
                username = username
            )
                .onStart {
                    _membersState.value = Resource.Loading
                }
                .catch { e ->
                    _membersState.value =
                        Resource.Failure(e.message ?: "Error al cargar los members")
                }
                .collectLatest { result ->
                    _membersState.value = result

                    if (result is Resource.Success) {
                        _localMembersList.value = result.data
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
    // CREATE PRODUCT
    // ---------------------------------------------
    fun createMember(
        request: UserCollabCreateRequest
    ) {
        viewModelScope.launch {
            _createMemberState.value = Resource.Loading

            when (val result = authUseCase.createColaboradorUC(request)) {
                is Resource.Success -> {
                    _createMemberState.value = result

                }

                is Resource.Failure -> {
                    _createMemberState.value = result
                }

                else -> {}
            }
        }
    }


    // ---------------------------------------------
    // CREATE PRODUCT
    // ---------------------------------------------
    fun getBusinessById(
       businessId: Int,
       userId:Int
    ) {
        viewModelScope.launch {
            _businessState.value = Resource.Loading

            when (val result = authUseCase
                .getBusinessByIdUC(
                    businessId=businessId,
                    userId = userId
                )
            ) {
                is Resource.Success -> {
                    _businessState.value = result

                }

                is Resource.Failure -> {
                    _businessState.value = result
                }

                else -> {}
            }
        }
    }

    // ---------------------------------------------
    // RESET STATES
    // ---------------------------------------------
    fun resetCreateState() {
        _createMemberState.value = Resource.Idle
    }

}
