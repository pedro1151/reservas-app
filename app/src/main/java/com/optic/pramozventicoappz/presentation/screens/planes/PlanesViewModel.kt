package com.optic.pramozventicoappz.presentation.screens.googleplaybilling

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.optic.pramozventicoappz.domain.model.external.googleplaybilling.GooglePlayPurchaseResponse
import com.optic.pramozventicoappz.domain.model.external.googleplaybilling.GooglePlayVerifyRequest
import com.optic.pramozventicoappz.domain.model.external.googleplaybilling.GooglePlayVerifyResponse
import com.optic.pramozventicoappz.domain.model.external.googleplaybilling.HasEntitlementResponse
import com.optic.pramozventicoappz.domain.model.external.googleplaybilling.UserEntitlementResponse
import com.optic.pramozventicoappz.domain.model.planes.BillingMode
import com.optic.pramozventicoappz.domain.model.planes.PlanType
import com.optic.pramozventicoappz.domain.useCase.external.ExternalUseCase
import com.optic.pramozventicoappz.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GooglePlayBillingViewModel @Inject constructor(
    private val externalUC: ExternalUseCase
) : ViewModel() {

    private val TAG = "GP_BILLING_VM"

    private val _verifyPurchaseState =
        MutableStateFlow<Resource<GooglePlayVerifyResponse>>(Resource.Idle)
    val verifyPurchaseState: StateFlow<Resource<GooglePlayVerifyResponse>> =
        _verifyPurchaseState.asStateFlow()

    private val _userPurchasesState =
        MutableStateFlow<Resource<List<GooglePlayPurchaseResponse>>>(Resource.Idle)
    val userPurchasesState: StateFlow<Resource<List<GooglePlayPurchaseResponse>>> =
        _userPurchasesState.asStateFlow()

    private val _userEntitlementsState =
        MutableStateFlow<Resource<List<UserEntitlementResponse>>>(Resource.Idle)
    val userEntitlementsState: StateFlow<Resource<List<UserEntitlementResponse>>> =
        _userEntitlementsState.asStateFlow()

    private val _hasEntitlementState =
        MutableStateFlow<Resource<HasEntitlementResponse>>(Resource.Idle)
    val hasEntitlementState: StateFlow<Resource<HasEntitlementResponse>> =
        _hasEntitlementState.asStateFlow()

    private val _selectedPlan = MutableStateFlow<PlanType?>(null)
    val selectedPlan: StateFlow<PlanType?> = _selectedPlan.asStateFlow()

    private val _selectedBillingMode = MutableStateFlow(BillingMode.YEARLY)
    val selectedBillingMode: StateFlow<BillingMode> = _selectedBillingMode.asStateFlow()

    private val _selectedProductId = MutableStateFlow<String?>(null)
    val selectedProductId: StateFlow<String?> = _selectedProductId.asStateFlow()

    fun selectPlan(plan: PlanType) {
        Log.d(TAG, "📌 selectPlan | plan=$plan")
        _selectedPlan.value = plan
        refreshSelectedProductId()
    }

    fun selectBillingMode(mode: BillingMode) {
        Log.d(TAG, "📌 selectBillingMode | mode=$mode")
        _selectedBillingMode.value = mode
        refreshSelectedProductId()
    }

    private fun refreshSelectedProductId() {
        val plan = _selectedPlan.value
        val mode = _selectedBillingMode.value

        val productId = buildProductId(plan, mode)

        Log.d(TAG, "🧾 selectedProductId=$productId")

        _selectedProductId.value = productId
    }

    fun buildProductId(
        plan: PlanType?,
        mode: BillingMode
    ): String? {
        return when {
            plan == PlanType.STANDARD && mode == BillingMode.MONTHLY -> "standard-monthly"
            plan == PlanType.STANDARD && mode == BillingMode.YEARLY -> "standard-yearly"

            plan == PlanType.PRO && mode == BillingMode.MONTHLY -> "pro-monthly"
            plan == PlanType.PRO && mode == BillingMode.YEARLY -> "pro-annual"

            plan == PlanType.GOLD && mode == BillingMode.MONTHLY -> "gold-monthly"
            plan == PlanType.GOLD && mode == BillingMode.YEARLY -> "gold-yearly"

            else -> null
        }
    }

    fun verifyPurchase(request: GooglePlayVerifyRequest) {
        viewModelScope.launch {
            Log.d(TAG, "🚀 verifyPurchase | product=${request.productId}")

            _verifyPurchaseState.value = Resource.Loading

            try {
                val result = externalUC.googlePlayVerifyPurchasesUC(request)
                _verifyPurchaseState.value = result

                when (result) {
                    is Resource.Success -> {
                        Log.d(TAG, "✅ verifyPurchase SUCCESS")
                        loadUserEntitlements(true)
                    }

                    is Resource.Failure -> {
                        Log.e(TAG, "❌ verifyPurchase FAILURE: ${result.message}")
                    }

                    else -> Unit
                }

            } catch (e: Exception) {
                Log.e(TAG, "💥 verifyPurchase EXCEPTION", e)
                _verifyPurchaseState.value =
                    Resource.Failure(e.localizedMessage ?: "Error verificando compra")
            }
        }
    }

    fun buildVerifyRequestFromPurchaseToken(
        purchaseToken: String,
        businessId: Int
    ): GooglePlayVerifyRequest? {
        val productId = _selectedProductId.value ?: return null

        return GooglePlayVerifyRequest(
            productId = productId,
            productType = "subscription",
            purchaseToken = purchaseToken,
            businessId = businessId
        )
    }

    fun verifySelectedPurchase(
        purchaseToken: String,
        businessId: Int
    ) {
        val request = buildVerifyRequestFromPurchaseToken(purchaseToken = purchaseToken, businessId=businessId)

        if (request == null) {
            Log.e(TAG, "❌ verifySelectedPurchase | productId null")
            _verifyPurchaseState.value = Resource.Failure("No hay producto seleccionado")
            return
        }

        verifyPurchase(request)
    }

    fun loadUserPurchases(
        onlyActive: Boolean = false,
        limit: Int = 50,
        offset: Int = 0
    ) {
        viewModelScope.launch {
            Log.d(TAG, "📦 loadUserPurchases | onlyActive=$onlyActive")

            _userPurchasesState.value = Resource.Loading

            try {
                val result = externalUC.getUserPurchasesUC(
                    onlyActive = onlyActive,
                    limit = limit,
                    offset = offset
                )

                _userPurchasesState.value = result

                when (result) {
                    is Resource.Success -> {
                        Log.d(TAG, "✅ loadUserPurchases SUCCESS size=${result.data.size}")
                    }

                    is Resource.Failure -> {
                        Log.e(TAG, "❌ loadUserPurchases FAILURE: ${result.message}")
                    }

                    else -> Unit
                }

            } catch (e: Exception) {
                Log.e(TAG, "💥 loadUserPurchases EXCEPTION", e)
                _userPurchasesState.value =
                    Resource.Failure(e.localizedMessage ?: "Error cargando compras")
            }
        }
    }

    fun loadUserEntitlements(
        onlyActive: Boolean = true
    ) {
        viewModelScope.launch {
            Log.d(TAG, "🎯 loadUserEntitlements | onlyActive=$onlyActive")

            _userEntitlementsState.value = Resource.Loading

            try {
                val result = externalUC.getUserEntitlementsUC(onlyActive)
                _userEntitlementsState.value = result

                when (result) {
                    is Resource.Success -> {
                        Log.d(TAG, "✅ loadUserEntitlements SUCCESS size=${result.data.size}")
                    }

                    is Resource.Failure -> {
                        Log.e(TAG, "❌ loadUserEntitlements FAILURE: ${result.message}")
                    }

                    else -> Unit
                }

            } catch (e: Exception) {
                Log.e(TAG, "💥 loadUserEntitlements EXCEPTION", e)
                _userEntitlementsState.value =
                    Resource.Failure(e.localizedMessage ?: "Error cargando planes")
            }
        }
    }

    fun checkEntitlement(entitlement: String) {
        viewModelScope.launch {
            Log.d(TAG, "🔎 checkEntitlement | $entitlement")

            _hasEntitlementState.value = Resource.Loading

            try {
                val result = externalUC.getMyEntitlementUC(entitlement)
                _hasEntitlementState.value = result

                when (result) {
                    is Resource.Success -> {
                        Log.d(TAG, "✅ checkEntitlement RESULT: ${result.data.hasEntitlement}")
                    }

                    is Resource.Failure -> {
                        Log.e(TAG, "❌ checkEntitlement FAILURE: ${result.message}")
                    }

                    else -> Unit
                }

            } catch (e: Exception) {
                Log.e(TAG, "💥 checkEntitlement EXCEPTION", e)
                _hasEntitlementState.value =
                    Resource.Failure(e.localizedMessage ?: "Error verificando plan")
            }
        }
    }

    fun resetVerifyState() {
        _verifyPurchaseState.value = Resource.Idle
    }

    fun resetAllStates() {
        Log.d(TAG, "♻️ resetAllStates")
        _verifyPurchaseState.value = Resource.Idle
        _userPurchasesState.value = Resource.Idle
        _userEntitlementsState.value = Resource.Idle
        _hasEntitlementState.value = Resource.Idle
        _selectedPlan.value = null
        _selectedBillingMode.value = BillingMode.YEARLY
        _selectedProductId.value = null
    }
}