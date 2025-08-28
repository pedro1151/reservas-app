package com.optic.ecommerceappmvvm.presentation.screens.team.components.teamFixture

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.optic.ecommerceappmvvm.domain.model.fixture.FixtureResponse
import com.optic.ecommerceappmvvm.domain.util.Resource
import com.optic.ecommerceappmvvm.presentation.screens.fixtures.list.FixtureList

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TeamFixture(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    fixtureState: Resource<List<FixtureResponse>>,
    title: String = "Siguiendo",
    paddingValues: PaddingValues
) {

    FixtureList(
        modifier = modifier,
        navController = navController,
        fixtureState = fixtureState,
        title = "Partidos"
    )
}