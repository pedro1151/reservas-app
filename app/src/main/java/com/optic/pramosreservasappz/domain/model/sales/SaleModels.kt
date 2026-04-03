package com.optic.pramosreservasappz.domain.model.sales


import com.google.gson.annotations.SerializedName


data class SaleCreateRequest(

    @SerializedName("owner_id") val ownerId: Int? = null,
    @SerializedName("created_by_user_id") val createdByUserId: Int? = null,
    @SerializedName("local_id") val localId: String? = null,
    @SerializedName("amount") val amount: Double,
    @SerializedName("description") val description: String? = null,
    @SerializedName("client_id") val clientId: Int? = null,
    @SerializedName("payment_method") val paymentMethod: String? = "cash",
    @SerializedName("created_by") val createdBy: String? = null
)

data class SaleUpdateRequest(

    @SerializedName("amount") val amount: Double? = null,
    @SerializedName("description") val description: String? = null,
    @SerializedName("client_id") val clientId: Int? = null,
    @SerializedName("payment_method") val paymentMethod: String? = null,
    @SerializedName("updated_by") val updatedBy: String? = null
)

data class SaleResponse(

    @SerializedName("id") val id: Int,
    @SerializedName("owner_id") val ownerId: Int?,
    @SerializedName("created_by_user_id") val createdByUserId: Int?,
    @SerializedName("local_id") val localId: String?,
    @SerializedName("amount") val amount: Double,
    @SerializedName("description") val description: String?,
    @SerializedName("client_id") val clientId: Int?,
    @SerializedName("payment_method") val paymentMethod: String,
    @SerializedName("is_synced") val isSynced: Boolean,
    @SerializedName("deleted") val deleted: String?, // ISO date
    @SerializedName("created_by") val createdBy: String?,
    @SerializedName("created") val created: String,
    @SerializedName("updated_by") val updatedBy: String?,
    @SerializedName("updated") val updated: String
)


data class SaleItemCreateRequest(

    @SerializedName("sale_id") val saleId: Int,
    @SerializedName("product_id") val productId: Int? = null,
    @SerializedName("quantity") val quantity: Int = 1,
    @SerializedName("price") val price: Double,
    @SerializedName("created_by") val createdBy: String? = null
)

data class SaleItemCreateWithoutSaleId(

    @SerializedName("product_id") val productId: Int? = null,
    @SerializedName("quantity") val quantity: Int = 1,
    @SerializedName("price") val price: Double,
    @SerializedName("created_by") val createdBy: String? = null
)

data class CreateSaleWithItemsRequest(

    @SerializedName("sale") val sale: SaleCreateRequest,
    @SerializedName("items") val items: List<SaleItemCreateWithoutSaleId>
)

data class ProductResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("price") val price: Double
)

data class SaleItemComplete(
    @SerializedName("id")
    val id: Int,
    @SerializedName("quantity")
    val quantity: Int,
    @SerializedName("price")
    val price: Double,
    @SerializedName("product")
    val product: ProductResponse?
)

data class SaleWithItemsResponse(

    @SerializedName("sale")
    val sale: SaleResponse,
    @SerializedName("items")
    val items: List<SaleItemComplete>
)