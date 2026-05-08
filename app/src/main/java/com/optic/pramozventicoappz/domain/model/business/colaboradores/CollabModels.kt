package com.optic.pramozventicoappz.domain.model.business.colaboradores


import com.google.gson.annotations.SerializedName

data class UserCollabCreateRequest(

    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("username") val username: String,
    @SerializedName("business_id") val businessId: Int,
    @SerializedName("role") val role: String
)


data class UserCollabUpdateRequest(

    @SerializedName("user_id") val userId:Int,
    @SerializedName("email") val email:  String,
    @SerializedName("username") val username:String,
    @SerializedName("password") val password:String,

    @SerializedName("business_member_id") val businessMemberId:Int,
    @SerializedName("is_active") val isActive: Boolean,
    @SerializedName("updated_by") val updatedBy: String,
    @SerializedName("role") val role: String
)


data class UserResponse(

    @SerializedName("id")
    val id: Int,

    @SerializedName("email")
    val email: String,

    @SerializedName("username")
    val username: String
)

data class BusinessMemberResponse(

    @SerializedName("id")
    val id: Int,

    @SerializedName("role")
    val role: String,

    @SerializedName("is_active")
    val isActive: Boolean
) {
    val roleLabel: String
        get() = when (role.lowercase()) {
            "admin" -> "Administrador"
            "collaborator" -> "Vendedor"
            "owner" -> "Gerente"
            else -> role
        }

    val statusLabel: String
        get() = if (isActive) "ACTIVO" else "BAJA"
}

data class UserMemberResponse(

    @SerializedName("user")
    val user: UserResponse,

    @SerializedName("business_member")
    val businessMember: BusinessMemberResponse
)







