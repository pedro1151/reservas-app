package com.optic.pramosreservasappz.presentation.screens.calendar.abmcalendar.steptree

import com.optic.pramosreservasappz.presentation.screens.calendar.ActivityViewModel
import com.optic.pramosreservasappz.presentation.screens.calendar.CalendarViewModel


import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateCalendarStepThreeScreen(
    navController: NavHostController,
    viewModel: CalendarViewModel = hiltViewModel(),
    activityViewModel: ActivityViewModel = hiltViewModel()
) {


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "STep 3",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        letterSpacing = (-0.5).sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color.White
    ) { paddingValues ->
        Text("setp 1")

    }
}

