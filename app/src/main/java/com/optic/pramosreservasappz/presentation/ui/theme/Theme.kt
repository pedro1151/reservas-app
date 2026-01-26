package com.optic.pramosreservasappz.presentation.ui.theme


import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Define aquí tus colores personalizados como Color de Compose
val Blue500 = Color(0xFF1565C0) // ejemplo
val Blue700 = Color(0xFF1976D2)
val Purple200 = Color(0xFFBB86FC)
val Purple700 = Color(0xFF3700B3)
val Teal200 = Color(0xFF03DAC5)
val Teal500 = Color(0xFF018786)
val WhiteMe = Color (0xFFFFFFFF)
val Grafito = Color(0xFF4A4A4A)
val azulMarino = Color(0xFF1565C0)
val LightBlack1 =  Color(0xFF1b1b1b)
val LightWhite1 = Color (0XFFe7e7e7)
val GrisModerno = Color(0xFF3A3A3A)



private val LightColorScheme = lightColorScheme(
    primary = LightBlack1,
    onPrimary = LightBlack1,
    primaryContainer = LightBlack1,
    onPrimaryContainer =LightBlack1,
    secondary = Color.Black,
    onSecondary =Color.White,
    background = Color.White,
    onBackground = LightBlack1,
    surface =  GrisModerno ,
    onSurface =  GrisModerno
)

private val DarkColorScheme = darkColorScheme(
    primary = LightBlack1,
    onPrimary = LightBlack1,
    primaryContainer = LightBlack1,
    onPrimaryContainer =LightBlack1,
    secondary = Color.Black,
    onSecondary =Color.White,
    background = Color.White,
    onBackground = LightBlack1,
    surface = GrisModerno,
    onSurface =  GrisModerno
)


@Composable
fun EcommerceAppMVVMTheme(
    themeMode: AppThemeMode = AppThemeMode.DARK,
    content: @Composable () -> Unit
) {
    val colorScheme = when (themeMode) {
        AppThemeMode.LIGHT -> LightColorScheme
        AppThemeMode.DARK -> DarkColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}