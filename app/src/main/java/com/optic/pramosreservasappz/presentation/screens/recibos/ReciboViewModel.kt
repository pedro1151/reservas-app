package com.optic.pramosreservasappz.presentation.screens.recibos


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
import android.util.Log
import com.optic.pramosreservasappz.domain.model.business.recibos.ReceiptType
import com.optic.pramosreservasappz.domain.model.sales.SaleWithItemsResponse

@HiltViewModel
class ReciboViewModel @Inject constructor(
    private val reservasUC: ReservasUC,
    private  val authUseCase: AuthUseCase
) : ViewModel() {



    private val _businessState = mutableStateOf<Resource<BusinessCompleteResponse>>(Resource.Idle)
    val businessState: State<Resource<BusinessCompleteResponse>> = _businessState

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
    // STATE: Obtener venta por ID
    // ---------------------------------------------
    private val _oneSaleState =
        mutableStateOf<Resource<SaleWithItemsResponse>>(Resource.Loading)
    val oneSaleState: State<Resource<SaleWithItemsResponse>> =
        _oneSaleState

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


    private val _receiptType = mutableStateOf(ReceiptType.STANDARD)
    val receiptType: State<ReceiptType> = _receiptType

    fun setReceiptType(type: ReceiptType) {
        _receiptType.value = type
    }



}
