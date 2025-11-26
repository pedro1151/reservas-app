package com.optic.ecommerceappmvvm.presentation.components.fixture.nextFixture


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.optic.ecommerceappmvvm.domain.model.fixture.FixtureResponse
import com.optic.ecommerceappmvvm.domain.util.Resource

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NextFixture(
    title: String = "Próximo partido",
    state: Resource<FixtureResponse>,
    navController: NavHostController
) {

    when (state) {
        is Resource.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        is Resource.Success -> {
            state.data?.let { fixture ->
                NextFixtureItem(
                    fixture = fixture,
                    title = "Próximo partido"
                )
            } ?: Text(
                "No se ha encontrado el próximo partido",
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
        }

        is Resource.Failure -> {
            Text(
                "No se ha encontrado el próximo partido",
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.error
            )
        }

        else -> {}
    }
}