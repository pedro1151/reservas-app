package com.optic.pramosfootballappz.presentation.screens.admin.home

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.optic.pramosfootballappz.presentation.navigation.graph.admin.AdminNavGraph
import com.optic.pramosfootballappz.presentation.screens.admin.home.components.AdminBottomBar

@Composable
fun AdminHomeScreen(navController: NavHostController = rememberNavController()) {
    Scaffold(
        bottomBar = { AdminBottomBar(navController = navController) }
    ) {  paddingValues ->
        AdminNavGraph(navController = navController)
    }
}