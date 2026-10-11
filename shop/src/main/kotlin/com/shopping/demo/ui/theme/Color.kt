package com.shopping.demo.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color


val ShoppingGreen = Color(0xFF356859)
val ShoppingGreenDark = Color(0xFF24483E)
val ShoppingGreenLight = Color(0xFF6F9989)

val ShoppingCoral = Color(0xFFE76D5A)
val ShoppingSand = Color(0xFFE9D8B5)

// Light theme
val LightBackground = Color(0xFFF8F6F1)
val LightSurface = Color(0xFFFFFFFF)
val LightSurfaceVariant = Color(0xFFEDEAE3)
val LightTextPrimary = Color(0xFF202522)
val LightTextSecondary = Color(0xFF737873)
val LightOutline = Color(0xFFD6D8D1)

// Dark theme
val DarkBackground = Color(0xFF151916)
val DarkSurface = Color(0xFF1E2420)
val DarkSurfaceVariant = Color(0xFF303832)
val DarkTextPrimary = Color(0xFFE4E9E3)
val DarkTextSecondary = Color(0xFFB5BDB5)
val DarkOutline = Color(0xFF626B63)

// Shared status colors
val ShoppingError = Color(0xFFBA1A1A)
val ShoppingDarkError = Color(0xFFFFB4AB)
val ShoppingSuccess = Color(0xFF386641)

val DiscountGradient = Brush.linearGradient(
    colors = listOf(
        Color(0xFFE57373),
        Color(0xFFC62828)
    )
)

val RatingGold = Color(0xFFE0A526)

val ErrorButtonGradient = Brush.linearGradient(
    colors = listOf(
        Color(0xFFEF5350),
        Color(0xFFE53935)
    )
)
