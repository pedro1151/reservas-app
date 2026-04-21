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
import com.optic.pramosreservasappz.domain.model.business.colaboradores.UserCollabUpdateRequest
import com.optic.pramosreservasappz.domain.model.business.colaboradores.UserMemberResponse
import com.optic.pramosreservasappz.domain.model.business.completebusiness.BusinessCompleteResponse
import com.optic.pramosreservasappz.domain.model.clients.ClientUpdateRequest
import com.optic.pramosreservasappz.domain.model.product.ProductResponse
import com.optic.pramosreservasappz.domain.model.product.ProductUpdateRequest
import com.optic.pramosreservasappz.domain.model.response.DefaultResponse
import com.optic.pramosreservasappz.domain.useCase.auth.AuthUseCase
import kotlinx.coroutines.delay
import android.util.Log
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

    private val _oneMemberState = mutableStateOf<Resource<UserMemberResponse>>(Resource.Idle)
    val oneMemberState: State<Resource<UserMemberResponse>> = _oneMemberState


    // ---------------------------------------------
    // FUNCIÓN: Actualizar cliente
    // ---------------------------------------------
    private val _updateMemberState =
        mutableStateOf<Resource<DefaultResponse>>(Resource.Idle)

    val updateMemberState: State<Resource<DefaultResponse>> =
        _updateMemberState
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




    fun getMember(userId: Int) {

        viewModelScope.launch {

            Log.d("BusinessVM", "getMember() iniciado")
            Log.d("BusinessVM", "userId recibido: $userId")

            _oneMemberState.value = Resource.Loading
            try {
                Log.d("BusinessVM", "Llamando authUseCase.getMemberUC(...)")
                when (val result = authUseCase.getMemberUC(
                    userId = userId
                )) {
                    is Resource.Success -> {

                        Log.d("BusinessVM", "SUCCESS getMember(): ${result.data}")
                        _oneMemberState.value = result
                    }
                    is Resource.Failure -> {

                        Log.e("BusinessVM", "FAILURE getMember(): ${result.message}")
                        _oneMemberState.value = result
                    }
                    is Resource.Loading -> {
                        Log.d("BusinessVM", "Estado Loading retornado")
                    }

                    is Resource.Idle -> {
                        Log.d("BusinessVM", "Estado Idle retornado")
                    }
                }

            } catch (e: Exception) {
                Log.e("BusinessVM", "EXCEPTION getMember(): ${e.message}", e)

                _oneMemberState.value =
                    Resource.Failure(
                        e.message ?: "Error inesperado al obtener member"
                    )
            }
        }
    }



    fun updateMember(
        request: UserCollabUpdateRequest
    ) {
        viewModelScope.launch {
            _updateMemberState.value = Resource.Loading
            when (val result = authUseCase.updateColaboradorUC(
                request = request
            )) {
                is Resource.Success -> {
                    _updateMemberState.value = result
                }
                is Resource.Failure -> {
                    _updateMemberState.value = result
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
