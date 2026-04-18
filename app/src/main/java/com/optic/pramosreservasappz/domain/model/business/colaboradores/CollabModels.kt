package com.optic.pramosreservasappz.domain.model.business.colaboradores


import com.google.gson.annotations.SerializedName

data class UserCollabCreateRequest(

    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String,

    @SerializedName("username")
    val username: String,

    @SerializedName("business_id")
    val businessId: Int,

    @SerializedName("role")
    val role: String
)


data class UserResponse(

    @SerializedName("id")
    val id: Int,

    @SerializedName("email")
    val email: String,

    @SerializedName("username")
    val username: String
)

data class UserMemberResponse(
    @SerializedName("business_member_id")
    val businessMemberId: Int,

    @SerializedName("user")
    val user: UserResponse,

    @SerializedName("role")
    val role: String
)









