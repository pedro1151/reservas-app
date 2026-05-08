package com.optic.pramozventicoappz.domain.model.product

import com.google.gson.annotations.SerializedName

data class ProductCreateRequest(
    @SerializedName("business_id")
    val businessId: Int? = null,

    @SerializedName("name")
    val name: String,

    @SerializedName("description")
    val description: String? = null,

    @SerializedName("type")
    val type: String = "product",

    @SerializedName("price")
    val price: Double,

    @SerializedName("stock")
    val stock: Int? = null,

    @SerializedName("created_by")
    val createdBy: String? = null
)

data class ProductUpdateRequest(
    @SerializedName("name")
    val name: String? = null,

    @SerializedName("price")
    val price: Double? = null,

    @SerializedName("stock")
    val stock: Int? = null,

    @SerializedName("is_active")
    val isActive: Boolean? = null,

    @SerializedName("updated_by")
    val updatedBy: String? = null,

    @SerializedName("description")
    val description: String? = null,

    @SerializedName("type")
    val type: String? = null
)

data class ProductResponse(
    @SerializedName("id")
    val id: Int,

    @SerializedName("business_id")
    val businessId: Int?,

    @SerializedName("name")
    val name: String,

    @SerializedName("description")
    val description: String?,

    @SerializedName("price")
    val price: Double,

    @SerializedName("stock")
    val stock: Int?,

    @SerializedName("is_active")
    val isActive: Boolean,

    @SerializedName("type")
    val type: String,

    @SerializedName("deleted")
    val deleted: String?,

    @SerializedName("deleted_by")
    val deletedBy: String?,

    @SerializedName("created_by")
    val createdBy: String?,

    @SerializedName("created")
    val created: String,

    @SerializedName("updated_by")
    val updatedBy: String?,

    @SerializedName("updated")
    val updated: String
)

data class MiniProductResponse(
    @SerializedName("id")
    val id: Int,

    @SerializedName("business_id")
    val businessId: Int?,

    @SerializedName("name")
    val name: String,

    @SerializedName("description")
    val description: String?,

    @SerializedName("price")
    val price: Double,

    @SerializedName("stock")
    val stock: Int?,

    @SerializedName("is_active")
    val isActive: Boolean,

    @SerializedName("type")
    val type: String
)