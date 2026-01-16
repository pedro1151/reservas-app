package com.optic.ecommerceappmvvm.presentation.screens.mas.components

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.optic.ecommerceappmvvm.presentation.MainActivity
import com.optic.ecommerceappmvvm.presentation.components.LoginLinkCard
import com.optic.ecommerceappmvvm.presentation.settings.idiomas.IdiomaSelectionBottomSheet
import com.optic.ecommerceappmvvm.presentation.navigation.screen.client.ClientScreen
import com.optic.ecommerceappmvvm.presentation.screens.mas.MasViewModel
import com.optic.ecommerceappmvvm.presentation.settings.idiomas.AppLanguage
import com.optic.ecommerceappmvvm.presentation.settings.idiomas.LocalAppLanguage
import com.optic.ecommerceappmvvm.presentation.ui.theme.LocalAppTheme
import com.optic.ecommerceappmvvm.presentation.ui.theme.components.ThemeSelectionBottomSheet
import com.optic.ecommerceappmvvm.R
@Composable
fun MasContent(modifier: Modifier
               ,navController: NavHostController
               ,isAuthenticated: Boolean

) {
    val activity = LocalContext.current as? Activity
    val vm: MasViewModel = hiltViewModel()

    // Estado que controla el tema de la app
    val themeState = LocalAppTheme.current
    val showSheet = remember { mutableStateOf(false) }


    val languageState = LocalAppLanguage.current


    if (showSheet.value) {
        ThemeSelectionBottomSheet(
            currentTheme = themeState.value,
            onThemeSelected = {
                themeState.value = it
                showSheet.value = false // cierra el popup
            },
            onDismiss = { showSheet.value = false }
        )
    }


    // Estado que controla el idioma de la app
    //val idiomaState = LocalAppTheme.current
    val showSheetIdioma = remember { mutableStateOf(false) }

    if (showSheetIdioma.value) {
        IdiomaSelectionBottomSheet(
            currentLanguage = languageState.value,
            onLanguageSelected = { selected ->
                languageState.value = selected
                showSheetIdioma.value = false
            },
            onDismiss = { showSheetIdioma.value = false }
        )
    }


    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 1.dp, vertical = 1.dp),
        verticalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        // 🔴 NUEVO: Cerrar sesión
        if (!isAuthenticated) {
            MasCardItem(
                title = R.string.login_title.toString(),
                icon = Icons.Default.AccountBox,
                onClick = { navController.navigate(ClientScreen.Login.route)}  // <-- disparador para mostrar el bottom sheet
            )

        }
        MasCardItem(
            title = "Tema",
            icon = Icons.Default.FavoriteBorder,
            onClick = { showSheet.value = true }  // <-- disparador para mostrar el bottom sheet
        )
        MasCardItem(
            title = "Seleccionar Idioma",
            icon = Icons.Default.List,
            onClick = { showSheetIdioma.value = true }
        )
        MasCardItem(title = "Compartir UNIFOT", icon = Icons.Default.Share)
        MasCardItem(title = "Síguenos", icon = Icons.Default.Person)
        MasCardItem(title = "Política de Privacidad", icon = Icons.Default.Info)
        MasCardItem(title = "Versión de la app", icon = Icons.Default.Info)

        // 🔴 NUEVO: Cerrar sesión
        if (isAuthenticated) {
            MasCardItem(
                title = "Cerrar sesión",
                icon = Icons.Default.Logout,
            ) {
                vm.logout()
                activity?.finish()
                activity?.startActivity(Intent(activity, MainActivity::class.java))
            }
        }
    }
}

@Composable
fun MasCardItem(
    icon: ImageVector,
    title: String,
    modifier: Modifier = Modifier,
    iconTint: Color = MaterialTheme.colorScheme.onSurfaceVariant, // Gris moderno
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(horizontal = 1.dp, vertical = 1.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start // Podés usar Center si querés centrarlos horizontalmente
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Normal,
                    fontSize = 13.sp
                ),
                color = textColor
            )
        }
    }
}