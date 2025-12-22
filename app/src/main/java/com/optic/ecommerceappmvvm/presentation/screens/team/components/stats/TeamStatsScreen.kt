package com.optic.ecommerceappmvvm.presentation.screens.team.components.stats

import com.optic.ecommerceappmvvm.presentation.screens.team.TeamViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.optic.ecommerceappmvvm.domain.model.team.TeamStatsResponse
import com.optic.ecommerceappmvvm.domain.util.Resource
import com.optic.ecommerceappmvvm.presentation.ui.theme.getGreenColorFixture
import com.optic.ecommerceappmvvm.presentation.ui.theme.getRedCardColor
import com.optic.ecommerceappmvvm.presentation.ui.theme.getYellowCardColor

// ---------------------------------------------------------
// MAIN SCREEN
// ---------------------------------------------------------
@Composable
fun TeamStatsScreen(
    paddingValues: PaddingValues,
    teamId: Int,
    season: Int,
    date: String?
) {
    val viewModel: TeamViewModel = hiltViewModel()
    val statsState by viewModel.teamStatsState.collectAsState()

    LaunchedEffect(teamId, season, date) {
        viewModel.getTeamStats(season, teamId, date)
    }

    when (statsState) {
        is Resource.Loading -> LoadingView(paddingValues)
        is Resource.Failure -> ErrorView((statsState as Resource.Failure).message, paddingValues)
        is Resource.Success -> {
            val data = (statsState as Resource.Success<TeamStatsResponse>).data?.response
            if (data == null) {
                EmptyView(paddingValues)
                return
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 1.dp, horizontal = 1.dp)
                    .background(MaterialTheme.colorScheme.background),
                contentPadding = PaddingValues(1.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                item { FixturesCard(fixtures = data.fixtures) }
                item { GoalsCard(goals = data.goals) }
                item { BiggestCard(biggest = data.biggest) }
                item { CleanSheetFailedCard(clean = data.cleanSheet, failed = data.failedToScore) }
                item { PenaltyCard(penalty = data.penalty) }
                item { LineupsCard(lineups = data.lineups ?: emptyList()) }
                item { CardsCard(cards = data.cards) }
            }
        }

        else -> {}
    }
}

// ---------------------------------------------------------
// GENERIC UI SECTIONS
// ---------------------------------------------------------
@Composable
fun LoadingView(padding: PaddingValues) {
    Box(
        modifier = Modifier.fillMaxSize().padding(padding),
        contentAlignment = Alignment.Center
    ) { CircularProgressIndicator() }
}

@Composable
fun ErrorView(message: String?, padding: PaddingValues) {
    Box(
        modifier = Modifier.fillMaxSize().padding(padding),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message ?: "Error al cargar estadísticas",
            color = MaterialTheme.colorScheme.error
        )
    }
}

@Composable
fun EmptyView(padding: PaddingValues) {
    Box(
        modifier = Modifier.fillMaxSize().padding(padding),
        contentAlignment = Alignment.Center
    ) { Text("No hay estadísticas disponibles") }
}

// ---------------------------------------------------------
// HELPERS (100% COMPATIBLES KOTLIN 1.9.24)
// ---------------------------------------------------------

fun Map<String, Any>?.map(vararg keys: String): Map<String, Any>? {
    var current: Any? = this
    for (k in keys) {
        current = (current as? Map<*, *>)?.get(k)
    }
    return current as? Map<String, Any>
}

fun Map<String, Any>?.int(vararg keys: String): Int? {
    var current: Any? = this
    for (k in keys) {
        current = (current as? Map<*, *>)?.get(k)
    }
    return when (current) {
        is Number -> current.toInt()
        is String -> current.toIntOrNull()
        else -> null
    }
}

fun Map<String, Any>?.string(vararg keys: String): String? {
    var current: Any? = this
    for (k in keys) {
        current = (current as? Map<*, *>)?.get(k)
    }
    return current?.toString()
}

// ---------------------------------------------------------
// GENERIC STATS COMPONENTS
// ---------------------------------------------------------
@Composable
fun StatCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(15.dp)
    ) {
        Text(
            text = title,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Spacer(modifier = Modifier.height(12.dp))
        content()
    }
}

@Composable
fun StatRowTriple(label: String, home: Int, away: Int, total: Int) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp) // <<< más espacio entre filas
    ) {

        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )

        Spacer(modifier = Modifier.height(6.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = home.toString(), modifier = Modifier.weight(1f), textAlign = TextAlign.Start)
            Text(text = away.toString(), modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
            Text(text = total.toString(), modifier = Modifier.weight(1f), textAlign = TextAlign.End)
        }
    }

    // Línea divisoria suave (solo si quieres elegancia)
    HorizontalDivider(
        thickness = 1.dp,
        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.10f)
    )
}

@Composable
fun StatRowTripleSofa(label: String, home: Int, away: Int, total: Int) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .padding(vertical = 6.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                modifier = Modifier.weight(1.3f),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                home.toString(),
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                away.toString(),
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                total.toString(),
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.End,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }

    HorizontalDivider(
        thickness = 1.dp,
        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.08f)
    )
}

// ---------------------------------------------------------
// CARDS
// ---------------------------------------------------------

@Composable
fun FixturesCard(fixtures: Map<String, Any>?) {
    StatCard(title = "Resultados") {

        // Encabezado tipo SofaScore
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp, vertical = 15.dp)
                .padding(bottom = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Categoría",
                modifier = Modifier.weight(1.3f),
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.getGreenColorFixture
            )
            Text(
                "Local",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.getGreenColorFixture
            )
            Text(
                "Visitante",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.getGreenColorFixture
            )
            Text(
                "Total",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.End,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.getGreenColorFixture
            )
        }

        HorizontalDivider(
            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.12f),
            thickness = 1.dp
        )

        Spacer(Modifier.height(4.dp))

        // --- Filas estilo SofaScore ---
        StatRowTripleSofa(
            "Jugados",
            (fixtures.int("played", "home") ?: 0),
            (fixtures.int("played", "away") ?: 0),
            (fixtures.int("played", "total") ?: 0)
        )

        StatRowTripleSofa(
            "Ganados",
            (fixtures.int("wins", "home") ?: 0),
            (fixtures.int("wins", "away") ?: 0),
            (fixtures.int("wins", "total") ?: 0)
        )

        StatRowTripleSofa(
            "Empates",
            (fixtures.int("draws", "home") ?: 0),
            (fixtures.int("draws", "away") ?: 0),
            (fixtures.int("draws", "total") ?: 0)
        )

        StatRowTripleSofa(
            "Perdidos",
            (fixtures.int("loses", "home") ?: 0),
            (fixtures.int("loses", "away") ?: 0),
            (fixtures.int("loses", "total") ?: 0)
        )
    }
}

// ---------------------------------------------------------
// GOALS CARD — ESTILO FOTMOB MODERNO
// ---------------------------------------------------------

@Composable
fun GoalsCard(goals: Map<String, Any>?) {

    val goalsFor = goals?.map("for")
    val goalsAgainst = goals?.map("against")

    StatCard(title = "Goles") {

        // Tabla de resumen
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 15.dp, vertical = 15.dp)
                .padding(bottom = 15.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "Local",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Visitante",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Total",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.End,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // --- Goles a favor ---
        StatRowTriple(
            "A favor",
            (goalsFor.int("total", "home") ?: 0),
            (goalsFor.int("total", "away") ?: 0),
            (goalsFor.int("total", "total") ?: 0)
        )

        // --- Goles en contra ---
        StatRowTriple(
            "En contra",
            (goalsAgainst.int("total", "home") ?: 0),
            (goalsAgainst.int("total", "away") ?: 0),
            (goalsAgainst.int("total", "total") ?: 0)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // --- Minutos estilo FotMob ---
        Text("Por Minutos", fontWeight = FontWeight.Bold, fontSize = 16.sp)

        Spacer(modifier = Modifier.height(8.dp))

        val minuteMap = goalsFor.map("minute") ?: emptyMap()

        minuteMap.forEach { (range, data) ->
            val d = data as? Map<String, Any> ?: return@forEach

            val total = d.int("total") ?: 0
            val pct = d.string("percentage") ?: "-"

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(range, fontWeight = FontWeight.SemiBold)
                Text("$total", textAlign = TextAlign.Center)
                Text(pct, textAlign = TextAlign.End, fontWeight = FontWeight.Medium)
            }
        }
    }
}

@Composable
fun StatRowSingleSofa(
    title: String,
    value: Any
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp, vertical = 15.dp)
            .padding(bottom = 15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            title,
            modifier = Modifier.weight(1.3f),
            fontWeight = FontWeight.Medium
        )
        Text(
            "$value",
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.End,
            fontWeight = FontWeight.Bold
        )
    }
}


@Composable
fun BiggestCard(biggest: Map<String, Any>?) {

    val streak = biggest?.map("streak")
    val wins = biggest?.map("wins")

    StatCard(title = "Mayor racha") {

        // Encabezado tipo SofaScore
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp, vertical = 15.dp)
                .padding(bottom = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Categoría",
                modifier = Modifier.weight(1.3f),
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.getGreenColorFixture
            )
            Text(
                "Valor",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.End,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.getGreenColorFixture
            )
        }

        HorizontalDivider(
            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.12f),
            thickness = 1.dp
        )

        Spacer(Modifier.height(4.dp))

        // Rachas
        StatRowSingleSofa(
            "Ganados seguidos",
            streak.int("wins") ?: 0
        )
        StatRowSingleSofa(
            "Empates seguidos",
            streak.int("draws") ?: 0
        )
        StatRowSingleSofa(
            "Perdidos seguidos",
            streak.int("loses") ?: 0
        )

        Spacer(Modifier.height(16.dp))

        // Título secundario tipo SofaScore
        Text(
            "Victorias más amplias",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = MaterialTheme.colorScheme.getGreenColorFixture
        )

        Spacer(Modifier.height(8.dp))

        // Filas estilo SofaScore
        StatRowSingleSofa(
            title = "En casa",
            value = wins.string("home") ?: "-"
        )
        StatRowSingleSofa(
            title = "Fuera",
            value = wins.string("away") ?: "-"
        )
    }
}


@Composable
fun CleanSheetFailedCard(
    clean: Map<String, Any>?,
    failed: Map<String, Any>?
) {
    StatCard(title = "Portería") {

        // ------ Encabezado tipo SofaScore ------
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp, vertical = 15.dp)
                .padding(bottom = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Categoría",
                modifier = Modifier.weight(1.3f),
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.getGreenColorFixture
            )
            Text(
                "Local",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.getGreenColorFixture
            )
            Text(
                "Visitante",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.getGreenColorFixture
            )
            Text(
                "Total",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.End,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.getGreenColorFixture
            )
        }

        HorizontalDivider(
            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.12f),
            thickness = 1.dp
        )

        Spacer(Modifier.height(6.dp))

        // ------ Filas estilo SofaScore ------
        clean.int("home")?.let {
            StatRowTripleSofa(
                "Portería a cero",
                it,
                clean.int("away")!!,
                clean.int("total")!!
            )
        }

        failed.int("home")?.let {
            StatRowTripleSofa(
                "Falló en marcar",
                it,
                failed.int("away")!!,
                failed.int("total")!!
            )
        }
    }
}


@Composable
fun PenaltyCard(penalty: Map<String, Any>?) {
    StatCard(title = "Penales") {

        // ------ Encabezado tipo SofaScore ------
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp, vertical = 15.dp)
                .padding(bottom = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Categoría",
                modifier = Modifier.weight(1.3f),
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.getGreenColorFixture
            )
            Text(
                "Local",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.getGreenColorFixture
            )
            Text(
                "Visitante",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.getGreenColorFixture
            )
            Text(
                "Total",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.End,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.getGreenColorFixture
            )
        }

        HorizontalDivider(
            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.12f),
            thickness = 1.dp
        )

        Spacer(Modifier.height(6.dp))

        // ------ Filas estilo SofaScore ------
        penalty.int("scored", "total")?.let {
            StatRowTripleSofa(
                "Convertidos",
                0,
                0,
                it
            )
        }

        penalty.int("missed", "total")?.let {
            StatRowTripleSofa(
                "Fallados",
                0,
                0,
                it
            )
        }

        penalty.int("total")?.let {
            StatRowTripleSofa(
                "Totales",
                0,
                0,
                it
            )
        }
    }
}


@Composable
fun LineupsCard(lineups: List<Map<String, Any>>?) {
    StatCard(title = "Formaciones usadas") {

        if (lineups.isNullOrEmpty()) {
            Text(
                "Sin datos",
                modifier = Modifier.padding(vertical = 12.dp),
                fontStyle = FontStyle.Italic,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            return@StatCard
        }

        // Encabezado tipo SofaScore
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp, vertical = 15.dp)
                .padding(bottom = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Formación",
                modifier = Modifier.weight(1.5f),
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.getGreenColorFixture
            )
            Text(
                "Partidos",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.End,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.getGreenColorFixture
            )
        }

       HorizontalDivider(
            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.12f),
            thickness = 1.dp
        )

        Spacer(Modifier.height(8.dp))

        // Filas estilo SofaScore
        lineups.forEachIndexed { index, lineup ->
            val formation = lineup.string("formation") ?: "-"
            val played = lineup.int("played") ?: 0

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                   // .animateItemPlacement(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    formation,
                    modifier = Modifier.weight(1.5f),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
                Text(
                    "$played",
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.End,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp
                )
            }

            if (index < lineups.lastIndex) {
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f),
                    thickness = 0.6.dp,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}


@Composable
fun CardsCard(cards: Map<String, Any>?) {

    val yellow = cards.map("yellow")
    val red = cards.map("red")

    StatCard(title = "Tarjetas") {

        // Encabezado estilo SofaScore
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp, vertical = 15.dp)
                .padding(bottom = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Período",
                modifier = Modifier.weight(1.2f),
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            )
            Text(
                "Amarillas",
                modifier = Modifier.weight(0.9f),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.getYellowCardColor
            )
            Text(
                "%",
                modifier = Modifier.weight(0.6f),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp
            )
            Text(
                "Rojas",
                modifier = Modifier.weight(0.9f),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.getRedCardColor
            )
            Text(
                "%",
                modifier = Modifier.weight(0.6f),
                textAlign = TextAlign.End,
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp
            )
        }

        HorizontalDivider(
            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.12f),
            thickness = 1.dp
        )

        Spacer(modifier = Modifier.height(6.dp))


        // ---------- FIX AQUÍ ----------
        val yellowKeys = yellow?.keys ?: emptySet()
        val redKeys = red?.keys ?: emptySet()

        val periods = (yellowKeys + redKeys).sorted()
        // --------------------------------


        periods.forEach { period ->

            val yData = yellow.map(period)
            val rData = red.map(period)

            val yTotal = yData.int("total")
            val yPct = yData.string("percentage") ?: "-"

            val rTotal = rData.int("total")
            val rPct = rData.string("percentage") ?: "-"

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp, vertical = 15.dp)
                    .padding(bottom = 15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    period,
                    modifier = Modifier.weight(1.2f),
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    yTotal.toString(),
                    modifier = Modifier.weight(0.9f),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.getYellowCardColor
                )

                Text(
                    yPct,
                    modifier = Modifier.weight(0.6f),
                    textAlign = TextAlign.Center
                )

                Text(
                    rTotal.toString(),
                    modifier = Modifier.weight(0.9f),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.getRedCardColor
                )

                Text(
                    rPct,
                    modifier = Modifier.weight(0.6f),
                    textAlign = TextAlign.End
                )
            }

            HorizontalDivider(
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.06f),
                thickness = 0.6.dp
            )
        }
    }
}
