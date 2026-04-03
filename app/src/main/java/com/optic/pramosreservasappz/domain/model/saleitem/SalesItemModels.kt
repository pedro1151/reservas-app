package com.optic.pramosreservasappz.domain.model.saleitem


import com.google.gson.annotations.SerializedName

open class SaleItemBase(

    @SerializedName("sale_id")
    val saleId: Int,

    @SerializedName("product_id")
    val productId: Int? = null,

    @SerializedName("quantity")
    val quantity: Int = 1,

    @SerializedName("price")
    val price: Double
)


data class SaleItemCreateRequest(

    @SerializedName("sale_id")
    val saleId: Int,

    @SerializedName("product_id")
    val productId: Int? = null,

    @SerializedName("quantity")
    val quantity: Int = 1,

    @SerializedName("price")
    val price: Double,

    @SerializedName("created_by")
    val createdBy: String? = null
)

data class SaleItemUpdateRequest(

    @SerializedName("product_id")
    val productId: Int? = null,

    @SerializedName("quantity")
    val quantity: Int? = null,

    @SerializedName("price")
    val price: Double? = null,

    @SerializedName("updated_by")
    val updatedBy: String? = null
)


data class SaleItemResponse(

    @SerializedName("id")
    val id: Int,

    @SerializedName("sale_id")
    val saleId: Int,

    @SerializedName("product_id")
    val productId: Int?,

    @SerializedName("quantity")
    val quantity: Int,

    @SerializedName("price")
    val price: Double,

    @SerializedName("created")
    val created: String,

    @SerializedName("updated")
    val updated: String
)