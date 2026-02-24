package com.optic.pramosreservasappz.domain.model.fixture.status

val LIVE_STATUSES = setOf(
    "1H",   // Primer tiempo
    "2H",   // Segundo tiempo
    "ET",   // Tiempo extra
    "AET",  // After Extra Time (aún en juego)
    "PEN",  // Penales (en juego)
    "LIVE"
)

val PAUSED_STATUSES = setOf(
    "HT",   // Half Time
    "INT"   // Interruption
)

val FINISHED_STATUSES = setOf(
    "FT", "AET", "PEN", "ABD", "Abd", "AWD", "WO"
)

val NOT_PLAYED_STATUSES = setOf(
    "NS", "PST", "CANC", "Canc", "SUSP", "TBD"
)