package com.optic.ecommerceappmvvm.presentation.screens.prode.topbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.optic.ecommerceappmvvm.presentation.ui.theme.getGreenLima

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProdeTopBar(
    title: String = "",
    navController: NavController,
    showTitle: Boolean = true,
    isSaving: Boolean,             // <-- 🔥 NUEVO
    isEditing: Boolean,
    onEditClick: () -> Unit,
    onSavingClick: () -> Unit
) {
    TopAppBar(
        title = {
            if (showTitle) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 16.dp),
                    contentAlignment = Alignment.TopStart
                ) {
                    Text(
                        text = title,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp
                    )
                }
            }
        },
        actions = {
                TextButton(
                    enabled = !isSaving,
                    onClick = if (isEditing) onSavingClick else  onEditClick,
                    modifier = Modifier
                            /*
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.getGreenColorFixture
                    )

                             */
                ){
                /*
                Icon(
                    imageVector = if (isEditing) Icons.Default.Check else Icons.Default.Edit,
                    contentDescription = "Editar",
                    tint = Color.White
                )

                 */
                Text(
                    text =if (isEditing) "Guardar" else "Editar",
                )
            }
        },

        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = "Volver",
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.primary,
            navigationIconContentColor = MaterialTheme.colorScheme.onSurface
        ),
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    )
}
