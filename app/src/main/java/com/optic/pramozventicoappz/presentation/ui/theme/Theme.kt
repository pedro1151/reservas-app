package com.optic.pramozventicoappz.presentation.ui.theme


import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Define aquí tus colores personalizados como Color de Compose

val Grafito = Color(0xFF4A4A4A)
val GrisModerno = Color(0xFF3A3A3A)
val BorderGray = Color(0xFFE5E7EB)
val GrisSuave = Color(0xFFE5E7EB)

val AmarrilloSuave =  Color(0xFFFFC857)
val TextPrimary = Color(0xFF0F172A)
val TextSecondary = Color(0xFF475569)
val  BadgeGrisBackground = Color(0xFFF8FAFC)
val BorderGrisSoftCard = Color(0xFFF1F5F9)
val AccentText = Color(0xFF8A6100)


val SoftCoolBackground = Brush.verticalGradient(
    listOf(
        Color(0xFFF9FAFB),
        Color(0xFFF1F5F9)
    )
)

val GradientBackground = Brush.verticalGradient(

        listOf(
          Color(0xFFE91E63),
        Color(0xFFD81B60)
        )

)

val ButtonSucessColor = Color(0xFF34D399)


val SuccessButtonBackground =   Brush.horizontalGradient(
    listOf(
        Color(0xFF34D399),
        Color(0xFF10B981)
    )
)

val UpgradeButtonBackground =   Brush.horizontalGradient(
    listOf(
        Color(0xFFFFD54F),
        Color(0xFFFFB300)
    )
)


private val LightColorScheme = lightColorScheme(
    primary =          Color(0xFFE91E63),
    onPrimary =Color(0xFFE5E7EB),
    primaryContainer =  Color(0xFFF8F4F6) ,
    onPrimaryContainer =Color(0xFF1b1b1b),
    secondary =               Color(0xFFFFB300),
    onSecondary =Color(0xFFE5E7EB),
    background = Color(0xFFF8F4F6),
    onBackground = Color(0xFF1b1b1b),
    surface =   Color.White,
    onSurface =  Color(0xFF4A4A4A)
)



private val DarkColorScheme = darkColorScheme(
    primary =          Color(0xFFE91E63),
    onPrimary =Color(0xFFE5E7EB),
    primaryContainer =  Color(0xFFF8F4F6) ,
    onPrimaryContainer =Color(0xFF1b1b1b),
    secondary =               Color(0xFFFFB300),
    onSecondary =Color(0xFFE5E7EB),
    background = Color(0xFFF8F4F6),
    onBackground = Color(0xFF1b1b1b),
    surface =   Color.White,
    onSurface =  Color(0xFF4A4A4A)
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