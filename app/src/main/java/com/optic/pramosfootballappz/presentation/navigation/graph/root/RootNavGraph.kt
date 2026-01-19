package com.optic.pramosfootballappz.presentation.navigation.graph.root

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.optic.pramosfootballappz.presentation.navigation.Graph
import com.optic.pramosfootballappz.presentation.navigation.graph.auth.AuthNavGraph
import com.optic.pramosfootballappz.presentation.navigation.graph.roles.RolesNavGraph

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RootNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        route = Graph.ROOT,
        startDestination = Graph.AUTH
    ) {
        AuthNavGraph(navController = navController)
        RolesNavGraph(navController = navController)



    }
}