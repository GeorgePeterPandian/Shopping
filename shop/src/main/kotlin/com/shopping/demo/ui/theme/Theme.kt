package com.shopping.demo.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val ColorForDarkText = Color(0xFFE0F0E5)

private val ShoppingLightColorScheme = lightColorScheme(
    primary = ShoppingGreen,
    onPrimary = LightSurface,
    primaryContainer = ShoppingGreenLight,
    onPrimaryContainer = ShoppingGreenDark,

    secondary = ShoppingCoral,
    onSecondary = LightSurface,
    secondaryContainer = ShoppingSand,
    onSecondaryContainer = LightTextPrimary,

    tertiary = ShoppingGreenLight,
    onTertiary = LightSurface,

    background = LightBackground,
    onBackground = LightTextPrimary,

    surface = LightSurface,
    onSurface = LightTextPrimary,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightTextSecondary,

    outline = LightOutline,
    error = ShoppingError,
    onError = LightSurface
)

private val ShoppingDarkColorScheme = darkColorScheme(
    primary = ShoppingGreenLight,
    onPrimary = ShoppingGreenDark,
    primaryContainer = ShoppingGreen,
    onPrimaryContainer = ColorForDarkText,

    secondary = Color(0xFFFF9B89),
    onSecondary = Color(0xFF571D16),
    secondaryContainer = Color(0xFF78382E),
    onSecondaryContainer = Color(0xFFFFDAD3),

    tertiary = Color(0xFFB4D0C0),
    onTertiary = Color(0xFF20372B),

    background = DarkBackground,
    onBackground = DarkTextPrimary,

    surface = DarkSurface,
    onSurface = DarkTextPrimary,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkTextSecondary,

    outline = DarkOutline,
    error = ShoppingDarkError,
    onError = Color(0xFF690005)
)



@Composable
fun ShoppingTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        ShoppingDarkColorScheme
    } else {
        ShoppingLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
