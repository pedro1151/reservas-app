package com.optic.pramozventicoappz.domain.model.external.googleplaybilling

import com.google.gson.annotations.SerializedName

data class GooglePlayVerifyRequest(
    @SerializedName("product_id") val productId: String,

    @SerializedName("product_type") val productType: String, // "subscription" | "inapp"

    @SerializedName("purchase_token") val purchaseToken: String,

    @SerializedName("business_id") val businessId: Int
)

data class GooglePlayPurchaseResponse(
    @SerializedName("id") val id: Int,

    @SerializedName("user_id") val userId: Int,

    @SerializedName("business_id") val businessId: Int,

    @SerializedName("package_name") val packageName: String,

    @SerializedName("product_id") val productId: String,

    @SerializedName("product_type") val productType: String,

    @SerializedName("order_id") val orderId: String? = null,

    @SerializedName("purchase_state") val purchaseState: String? = null,

    @SerializedName("acknowledgement_state") val acknowledgementState: String? = null,

    @SerializedName("is_active") val isActive: Boolean,

    @SerializedName("purchased_at") val purchasedAt: String? = null,

    @SerializedName("expires_at") val expiresAt: String? = null,

    @SerializedName("auto_renewing") val autoRenewing: Boolean? = null
)

data class UserEntitlementResponse(
    @SerializedName("id") val id: Int,

    @SerializedName("user_id") val userId: Int,

    @SerializedName("business_id") val businessId: Int,

    @SerializedName("entitlement") val entitlement: String,

    @SerializedName("source") val source: String,

    @SerializedName("purchase_id") val purchaseId: Int? = null,

    @SerializedName("is_active") val isActive: Boolean,

    @SerializedName("starts_at") val startsAt: String,

    @SerializedName("expires_at") val expiresAt: String? = null
)

data class GooglePlayVerifyResponse(
    @SerializedName("success") val success: Boolean,

    @SerializedName("message") val message: String,

    @SerializedName("entitlement") val entitlement: String? = null,

    @SerializedName("purchase") val purchase: GooglePlayPurchaseResponse? = null,

    @SerializedName("user_entitlement") val userEntitlement: UserEntitlementResponse? = null,

    @SerializedName("auth_plan_updated") val authPlanUpdated: Boolean = false
)

data class HasEntitlementResponse(
    @SerializedName("user_id") val userId: Int,

    @SerializedName("entitlement") val entitlement: String,

    @SerializedName("has_entitlement") val hasEntitlement: Boolean
)