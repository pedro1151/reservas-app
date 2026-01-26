package com.optic.pramosreservasappz.domain.model.player.stats

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PlayerStats(
    @SerializedName("id") val id: Int,
    @SerializedName("player_id") val playerId: Int,
    @SerializedName("season") val season: Int,

    @SerializedName("team") val team: Team,
    @SerializedName("league") val league: League,

    @SerializedName("games_position") val gamesPosition: String?,
    @SerializedName("games_appearences") val gamesAppearences: Int?,
    @SerializedName("games_lineups") val gamesLineups: Int?,
    @SerializedName("games_minutes") val gamesMinutes: Int?,
    @SerializedName("games_number") val gamesNumber: Int?,
    @SerializedName("games_rating") val gamesRating: Double?,
    @SerializedName("games_captain") val gamesCaptain: Boolean?,

    @SerializedName("goals_total") val goalsTotal: Int?,
    @SerializedName("goals_totalassists") val goalsTotalAssists: Int?,
    @SerializedName("goals_conceded") val goalsConceded: Int?,
    @SerializedName("goals_saves") val goalsSaves: Int?,

    @SerializedName("passes_total") val passesTotal: Int?,
    @SerializedName("passes_key") val passesKey: Int?,
    @SerializedName("passes_accuracy") val passesAccuracy: Int?,

    @SerializedName("tackles_total") val tacklesTotal: Int?,
    @SerializedName("tackles_blocks") val tacklesBlocks: Int?,
    @SerializedName("tackles_interceptions") val tacklesInterceptions: Int?,

    @SerializedName("duels_total") val duelsTotal: Int?,
    @SerializedName("duels_won") val duelsWon: Int?,

    @SerializedName("dribbles_attempts") val dribblesAttempts: Int?,
    @SerializedName("dribbles_success") val dribblesSuccess: Int?,
    @SerializedName("dribbles_past") val dribblesPast: Int?,

    @SerializedName("fouls_drawn") val foulsDrawn: Int?,
    @SerializedName("fouls_committed") val foulsCommitted: Int?,

    @SerializedName("shots_total") val shotsTotal: Int?,
    @SerializedName("shots_on") val shotsOn: Int?,

    @SerializedName("substitutes_in") val substitutesIn: Int?,
    @SerializedName("substitutes_out") val substitutesOut: Int?,
    @SerializedName("substitutes_bench") val substitutesBench: Int?,

    @SerializedName("cards_yellow") val cardsYellow: Int?,
    @SerializedName("cards_yellowred") val cardsYellowRed: Int?,
    @SerializedName("cards_red") val cardsRed: Int?,

    @SerializedName("penalty_won") val penaltyWon: Boolean?,
    @SerializedName("penalty_commited") val penaltyCommited: Boolean?,
    @SerializedName("penalty_scored") val penaltyScored: Int?,
    @SerializedName("penalty_missed") val penaltyMissed: Int?,
    @SerializedName("penalty_saved") val penaltySaved: Int?
) : Serializable