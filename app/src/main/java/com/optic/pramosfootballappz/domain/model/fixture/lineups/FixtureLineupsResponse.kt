package com.optic.pramosfootballappz.domain.model.fixture.lineups


import com.google.gson.annotations.SerializedName

data class FixtureLineupsResponse(
    @SerializedName("get")
    val get: String,

    @SerializedName("parameters")
    val parameters: Map<String, String>,

    @SerializedName("response")
    val response: List<FixtureLineupItem>
)

// --- Niveles internos (más anidados) ---

data class FixturePlayerInfo(
    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("number")
    val number: Int? = null,

    @SerializedName("pos")
    val pos: String? = null,

    @SerializedName("grid")
    val grid: String? = null,

    @SerializedName("photo")
    val photo: String? = null
)

data class FixturePlayerItem(
    @SerializedName("player")
    val player: FixturePlayerInfo
)

data class FixtureCoachInfo(
    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("photo")
    val photo: String? = null
)

data class FixtureTeamColors(
    @SerializedName("primary")
    val primary: String? = null,

    @SerializedName("number")
    val number: String? = null,

    @SerializedName("border")
    val border: String? = null
)

data class FixtureTeamColorRoles(
    @SerializedName("player")
    val player: FixtureTeamColors? = null,

    @SerializedName("goalkeeper")
    val goalkeeper: FixtureTeamColors? = null
)

data class FixtureTeamInfo(
    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("logo")
    val logo: String? = null,

    @SerializedName("colors")
    val colors: FixtureTeamColorRoles? = null
)

// --- Cada bloque de lineup (uno por team) ---
data class FixtureLineupItem(
    @SerializedName("team")
    val team: FixtureTeamInfo,

    @SerializedName("coach")
    val coach: FixtureCoachInfo,

    @SerializedName("formation")
    val formation: String? = null,

    @SerializedName("startXI")
    val startXI: List<FixturePlayerItem>,

    @SerializedName("substitutes")
    val substitutes: List<FixturePlayerItem>
)
