package com.optic.pramosreservasappz.domain.model.fixture.stats

import com.google.gson.annotations.SerializedName

// -------------------------
//   FixtureStatisticItem
// -------------------------
data class FixtureStatisticItem(
    @SerializedName("type")
    val type: String,

    @SerializedName("value")
    val value: Any? // Puede ser Int o String
)


// -------------------------
//   FixtureStatisticTeam
// -------------------------
data class FixtureStatisticTeam(
    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("logo")
    val logo: String?
)


// -------------------------
//   FixtureStatisticResponseItem
// -------------------------
data class FixtureStatisticResponseItem(
    @SerializedName("team")
    val team: FixtureStatisticTeam,

    @SerializedName("statistics")
    val statistics: List<FixtureStatisticItem>
)


// -------------------------
//   FixtureStatisticsResponse
// -------------------------
data class FixtureStatsResponse(
    @SerializedName("get")
    val get: String,

    @SerializedName("parameters")
    val parameters: Map<String, String>,

    @SerializedName("errors")
    val errors: List<String>,

    @SerializedName("results")
    val results: Int,

    @SerializedName("paging")
    val paging: Map<String, Int>,

    @SerializedName("response")
    val response: List<FixtureStatisticResponseItem>
)
