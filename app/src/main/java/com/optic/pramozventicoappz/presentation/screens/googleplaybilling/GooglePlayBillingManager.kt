package com.optic.pramozventicoappz.presentation.screens.googleplaybilling


import android.app.Activity
import android.content.Context
import android.util.Log
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClient.BillingResponseCode
import com.android.billingclient.api.BillingClient.ProductType
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams

class GooglePlayBillingManager(
    private val context: Context,
    private val onPurchaseTokenReceived: (purchaseToken: String) -> Unit,
    private val onBillingError: (message: String) -> Unit,
    private val onProductsLoaded: (Map<String, BillingProductUi>) -> Unit = {}
) : PurchasesUpdatedListener {

    private val tag = "GP_BILLING_MANAGER"

    private var billingClient: BillingClient = BillingClient.newBuilder(context)
        .setListener(this)
        .enablePendingPurchases()
        .build()

    private val productDetailsBySubscriptionId = mutableMapOf<String, ProductDetails>()
    private val offerDetailsByBasePlanId =
        mutableMapOf<String, ProductDetails.SubscriptionOfferDetails>()

    /**
     * En Play Console tienes 3 subscriptions:
     * - standard
     * - pro
     * - gold
     *
     * Y dentro de cada una tienes base plans:
     * - standard-monthly
     * - standard-yearly
     * - pro-monthly
     * - pro-annual
     * - gold-monthly
     * - gold-yearly
     */
    private val subscriptionIds = listOf(
        "standard",
        "pro",
        "gold"
    )

    fun startConnection() {
        if (billingClient.isReady) {
            Log.d(tag, "✅ BillingClient ya conectado")
            querySubscriptionProducts()
            return
        }

        Log.d(tag, "🚀 Conectando BillingClient...")

        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(result: BillingResult) {
                if (result.responseCode == BillingResponseCode.OK) {
                    Log.d(tag, "✅ BillingClient conectado")
                    querySubscriptionProducts()
                } else {
                    val message = "Error conectando BillingClient: ${result.debugMessage}"
                    Log.e(tag, "❌ $message")
                    onBillingError(message)
                }
            }

            override fun onBillingServiceDisconnected() {
                Log.w(tag, "⚠️ BillingClient desconectado")
            }
        })
    }

    private fun querySubscriptionProducts() {
        Log.d(tag, "📦 Consultando subscriptions en Google Play...")

        val productList = subscriptionIds.map { subscriptionId ->
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId(subscriptionId)
                .setProductType(ProductType.SUBS)
                .build()
        }

        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(productList)
            .build()

        billingClient.queryProductDetailsAsync(params) { result, productDetailsList ->
            if (result.responseCode != BillingResponseCode.OK) {
                val message = "Error consultando productos: ${result.debugMessage}"
                Log.e(tag, "❌ $message")
                onBillingError(message)
                return@queryProductDetailsAsync
            }

            productDetailsBySubscriptionId.clear()
            offerDetailsByBasePlanId.clear()

            productDetailsList.forEach { productDetails ->
                productDetailsBySubscriptionId[productDetails.productId] = productDetails

                productDetails.subscriptionOfferDetails?.forEach { offer ->
                    offerDetailsByBasePlanId[offer.basePlanId] = offer

                    Log.d(
                        tag,
                        "✅ Producto cargado | subscription=${productDetails.productId}, basePlan=${offer.basePlanId}"
                    )
                }
            }

            val uiProducts = buildUiProducts()
            onProductsLoaded(uiProducts)

            Log.d(tag, "✅ Productos cargados size=${uiProducts.size}")
        }
    }

    fun launchPurchase(
        activity: Activity,
        basePlanId: String
    ) {
        if (!billingClient.isReady) {
            val message = "BillingClient no está conectado todavía"
            Log.e(tag, "❌ $message")
            onBillingError(message)
            startConnection()
            return
        }

        val productDetails = findProductDetailsForBasePlan(basePlanId)
        val offerDetails = offerDetailsByBasePlanId[basePlanId]

        if (productDetails == null || offerDetails == null) {
            val message = "No se encontró el producto/basePlan: $basePlanId"
            Log.e(tag, "❌ $message")
            onBillingError(message)
            return
        }

        val offerToken = offerDetails.offerToken

        val productDetailsParams =
            BillingFlowParams.ProductDetailsParams.newBuilder()
                .setProductDetails(productDetails)
                .setOfferToken(offerToken)
                .build()

        val billingFlowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(listOf(productDetailsParams))
            .build()

        Log.d(tag, "🚀 launchBillingFlow | basePlanId=$basePlanId")

        val result = billingClient.launchBillingFlow(activity, billingFlowParams)

        if (result.responseCode != BillingResponseCode.OK) {
            val message = "No se pudo iniciar compra: ${result.debugMessage}"
            Log.e(tag, "❌ $message")
            onBillingError(message)
        }
    }

    private fun findProductDetailsForBasePlan(
        basePlanId: String
    ): ProductDetails? {
        return productDetailsBySubscriptionId.values.firstOrNull { productDetails ->
            productDetails.subscriptionOfferDetails?.any { offer ->
                offer.basePlanId == basePlanId
            } == true
        }
    }

    override fun onPurchasesUpdated(
        billingResult: BillingResult,
        purchases: MutableList<Purchase>?
    ) {
        when (billingResult.responseCode) {
            BillingResponseCode.OK -> {
                if (purchases.isNullOrEmpty()) {
                    Log.w(tag, "⚠️ Compra OK pero purchases vacío")
                    return
                }

                purchases.forEach { purchase ->
                    handlePurchase(purchase)
                }
            }

            BillingResponseCode.USER_CANCELED -> {
                Log.d(tag, "ℹ️ Usuario canceló la compra")
            }

            else -> {
                val message = "Error en compra: ${billingResult.debugMessage}"
                Log.e(tag, "❌ $message")
                onBillingError(message)
            }
        }
    }

    private fun handlePurchase(purchase: Purchase) {
        Log.d(
            tag,
            "🧾 handlePurchase | state=${purchase.purchaseState}, products=${purchase.products}"
        )

        if (purchase.purchaseState != Purchase.PurchaseState.PURCHASED) {
            Log.w(tag, "⚠️ Compra no completada todavía")
            return
        }

        val purchaseToken = purchase.purchaseToken

        if (purchaseToken.isBlank()) {
            val message = "purchaseToken vacío"
            Log.e(tag, "❌ $message")
            onBillingError(message)
            return
        }

        /**
         * Importante:
         * Tu backend valida con Google y activa STANDARD/PRO/GOLD.
         */
        Log.d(tag, "✅ purchaseToken recibido, enviando al backend...")
        onPurchaseTokenReceived(purchaseToken)

        /**
         * Recomendado:
         * Acknowledge para evitar reembolso automático.
         * Idealmente el acknowledge también puede hacerse en backend.
         * Para esta primera versión, lo hacemos desde Android después de recibir PURCHASED.
         */
        acknowledgeIfNeeded(purchase)
    }

    private fun acknowledgeIfNeeded(purchase: Purchase) {
        if (purchase.isAcknowledged) {
            Log.d(tag, "✅ Compra ya acknowledged")
            return
        }

        val params = AcknowledgePurchaseParams.newBuilder()
            .setPurchaseToken(purchase.purchaseToken)
            .build()

        billingClient.acknowledgePurchase(params) { result ->
            if (result.responseCode == BillingResponseCode.OK) {
                Log.d(tag, "✅ Compra acknowledged correctamente")
            } else {
                Log.e(tag, "❌ Error acknowledge: ${result.debugMessage}")
            }
        }
    }

    fun restorePurchases() {
        if (!billingClient.isReady) {
            Log.w(tag, "⚠️ restorePurchases llamado sin conexión")
            startConnection()
            return
        }

        billingClient.queryPurchasesAsync(
            com.android.billingclient.api.QueryPurchasesParams.newBuilder()
                .setProductType(ProductType.SUBS)
                .build()
        ) { result, purchases ->
            if (result.responseCode != BillingResponseCode.OK) {
                val message = "Error restaurando compras: ${result.debugMessage}"
                Log.e(tag, "❌ $message")
                onBillingError(message)
                return@queryPurchasesAsync
            }

            Log.d(tag, "✅ restorePurchases size=${purchases.size}")

            purchases.forEach { purchase ->
                handlePurchase(purchase)
            }
        }
    }

    fun endConnection() {
        if (billingClient.isReady) {
            Log.d(tag, "🔌 Cerrando BillingClient")
            billingClient.endConnection()
        }
    }

    private fun buildUiProducts(): Map<String, BillingProductUi> {
        val result = mutableMapOf<String, BillingProductUi>()

        offerDetailsByBasePlanId.forEach { (basePlanId, offerDetails) ->
            val lastPhase = offerDetails.pricingPhases.pricingPhaseList.lastOrNull()

            result[basePlanId] = BillingProductUi(
                basePlanId = basePlanId,
                formattedPrice = lastPhase?.formattedPrice ?: "",
                billingPeriod = lastPhase?.billingPeriod ?: "",
                recurrenceMode = lastPhase?.recurrenceMode ?: 0
            )
        }

        return result
    }
}

data class BillingProductUi(
    val basePlanId: String,
    val formattedPrice: String,
    val billingPeriod: String,
    val recurrenceMode: Int
)