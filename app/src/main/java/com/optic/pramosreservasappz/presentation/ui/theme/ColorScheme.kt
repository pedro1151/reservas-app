package com.optic.pramosreservasappz.presentation.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.ColorScheme

val FollowButtonBackgroundLight = Color(0xFFF2F2F2)
val FollowButtonTextLight = Color(0xFF4A4A4A)

val FollowButtonBackgroundDark = Color(0xFF8E24AA)
val FollowButtonTextDark = Color.White


val SelectedTextColorLight = Grafito
val SelectedIconColorLight = Grafito

val SelectedTextColorDark = Color(0XFF2dad0D)
val SelectedIconColorDark = Color(0XFF2dad0D)

val IconColorDark = Color(0xFFFF4D4D)
val IconColorLight = Color(0xFFFF4D4D)

val redColorFixture =  Color(0xFFF44336)
val greenColorFixture = Color(0xFF4CAF50)
val grayColorFixture = Color(0xFF4CAF50)

val greenLimaColor = Color(0xFF00C853)

val borderColorCardFixtureLight = Color(0xFF00C845)
val borderColorCardFixtureDark = Color(0xFF000000)

val ColorScheme.getYellowCardColor: Color
    @Composable get() = Color(0xFFF7D716)

val ColorScheme.getRedCardColor: Color
    @Composable get() = Color(0xFFE53935)

val ColorScheme.followButtonBackground: Color
    @Composable get() = when (LocalAppTheme.current.value) {
        AppThemeMode.DARK -> FollowButtonBackgroundDark
        AppThemeMode.LIGHT -> FollowButtonBackgroundLight
    }

val ColorScheme.followTextColor: Color
    @Composable get() = when (LocalAppTheme.current.value) {
        AppThemeMode.DARK -> FollowButtonTextDark
        AppThemeMode.LIGHT -> FollowButtonTextLight
    }


val ColorScheme.selectedTextColor: Color
    @Composable get() = when (LocalAppTheme.current.value) {
        AppThemeMode.DARK -> SelectedTextColorDark
        AppThemeMode.LIGHT -> SelectedTextColorLight
    }

val ColorScheme.selectedIconColor: Color
    @Composable get() = when (LocalAppTheme.current.value) {
        AppThemeMode.DARK -> SelectedIconColorDark
        AppThemeMode.LIGHT -> SelectedIconColorLight
    }

val ColorScheme.IconSecondaryColor: Color
    @Composable get() = when (LocalAppTheme.current.value) {
        AppThemeMode.DARK -> IconColorDark
        AppThemeMode.LIGHT -> IconColorLight
    }

val ColorScheme.getRedColorFixture  : Color
    @Composable get() = when (LocalAppTheme.current.value) {
        AppThemeMode.DARK -> redColorFixture
        AppThemeMode.LIGHT -> redColorFixture
    }

val ColorScheme.getGreenColorFixture  : Color
    @Composable get() = when (LocalAppTheme.current.value) {
        AppThemeMode.DARK -> greenColorFixture
        AppThemeMode.LIGHT -> greenColorFixture
    }

val ColorScheme.getGreenLima  : Color
    @Composable get() = when (LocalAppTheme.current.value) {
        AppThemeMode.DARK -> greenLimaColor
        AppThemeMode.LIGHT -> greenLimaColor
    }

val ColorScheme.getColorBorderFixture  : Color
    @Composable get() = when (LocalAppTheme.current.value) {
        AppThemeMode.DARK -> borderColorCardFixtureDark
        AppThemeMode.LIGHT -> borderColorCardFixtureLight
    }


