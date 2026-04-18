package com.optic.pramosreservasappz.presentation.ui.theme


import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.optic.pramosreservasappz.presentation.screens.salestats.colors.Cyan
import com.optic.pramosreservasappz.presentation.screens.salestats.colors.Purple

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
val BorderGray = Color(0xFFE5E7EB)
val GrisSuave = Color(0xFFE5E7EB)
val Green  = Color(0xFF1DB954)

val BluePrimary = Color(0xFF1E3A8A)
val BlueDark = Color(0xFF172554)
val BlueSoft = Color(0xFFEFF6FF)

val VioletPrimary = Color(0xFF7C3AED)

val BluePrimary2 = Color(0xFF2563EB)
val VioletAccent = Color(0xFF7C3AED)
val GradientStart = Color(0xFF2563EB)
val GradientEnd = Color(0xFF7C3AED)

val AmarrilloSuave =  Color(0xFFFFC857)
val ButtonGreen    =        Color(0xFF1DB954)
val Cyan = Color(0xFF22C1C3)
val CyanSoft = Color(0xFF22C1C3).copy(alpha = 0.12f)

val TextPrimary = Color(0xFF0F172A)
val TextSecondary = Color(0xFF475569)
val BorderSoft = Color(0xFFE2E8F0)
val BackgroundSoft = Color(0xFFF8FAFC)

val BlueGradient = Brush.horizontalGradient(
    listOf(
        Color(0xFF2563EB),
        Color(0xFF7C3AED),
      //  Color(0xFFD97706)
    )
)

val MoradoClaro = Color(0xFF7C3AED)
val MoradoOscuro = Color(0xFF5B3EBF)
val DarkPremiumGradient = Brush.horizontalGradient(
    listOf(
        Color(0xFF1F2937), // gris grafito
        Color(0xFF111827)  // casi negro (pero suave)
    )
)
val GreenGradient = Brush.horizontalGradient(
    listOf(
        Color(0xFF1DB954),
        Color(0xFF1DB954)
        //  Color(0xFFD97706)
    )
)

val SoftCoolBackground = Brush.verticalGradient(
    listOf(
        Color(0xFFF9FAFB),
        Color(0xFFF1F5F9)
    )
)

val GradientBackground = Brush.verticalGradient(
    listOf(
        Color(0xFF6D28D9),
        Color(0xFF0891B2)
    )
)

val ButtonBackground =     Brush.horizontalGradient(
    listOf(
        Color(0xFF22C1C3),
        Color(0xFF4ADEDE)
    )
)

val DiaglogBackground = Brush.verticalGradient(
    listOf(
        Color(0xFF22D3EE), // cyan brillante
        Color(0xFF06B6D4),
        Color(0xFF0EA5E9)  // toque azul
    )
)

val terciary = Color(0xFF6A5AE0)



private val LightColorScheme = lightColorScheme(
    primary =          Color(0xFF0891B2),
    onPrimary = LightBlack1,
    primaryContainer =  BluePrimary2  ,
    onPrimaryContainer =Color.White,
    secondary =           Color(0xFF0891B2),
    onSecondary =Color.White,
    background = Color.White,
    onBackground = LightBlack1,
    surface =  GrisModerno ,
    onSurface =  GrisModerno
)


private val DarkColorScheme = darkColorScheme(
    primary =          Color(0xFF0891B2),
    onPrimary = LightBlack1,
    primaryContainer =  BluePrimary2  ,
    onPrimaryContainer =Color.White,
    secondary =           Color(0xFF0891B2),
    onSecondary =Color.White,
    background = Color.White,
    onBackground = LightBlack1,
    surface =  GrisModerno ,
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