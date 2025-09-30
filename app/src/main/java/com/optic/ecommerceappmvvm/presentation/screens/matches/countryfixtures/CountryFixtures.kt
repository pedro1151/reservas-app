package com.optic.ecommerceappmvvm.presentation.screens.matches.countryfixtures

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.optic.ecommerceappmvvm.domain.model.fixture.FixtureResponse
import com.optic.ecommerceappmvvm.domain.util.Resource
import com.optic.ecommerceappmvvm.presentation.screens.fixtures.item.FixtureItem
import com.optic.ecommerceappmvvm.presentation.ui.theme.IconSecondaryColor

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CountryFixtures(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    fixtureState: Resource<List<FixtureResponse>>,
    title: String = "Locales"
) {
    var expanded by remember { mutableStateOf(true) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded },
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                    imageVector = Icons.Default.AccountBalance,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.IconSecondaryColor
            )

            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.primary
                ),
            )
        }

        AnimatedVisibility(
            visible = expanded,
            enter = expandVertically(animationSpec = tween(300)),
            exit = shrinkVertically(animationSpec = tween(300))
        ) {
            when (fixtureState) {
                is Resource.Loading -> {
                    CircularProgressIndicator()
                }

                is Resource.Success -> {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(1.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        (fixtureState.data ?: emptyList()).forEach { fixture ->
                            FixtureItem(
                                fixture = fixture,
                                navController = navController
                            )
                        }
                    }
                }

                is Resource.Failure -> {
                    Text(
                        text = "Error al cargar los Partidos",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}