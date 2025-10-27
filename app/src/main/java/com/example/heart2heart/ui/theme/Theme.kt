package com.example.heart2heart.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import com.example.heart2heart.R

@Composable
private fun getDarkColorScheme(darkTheme: Boolean): ColorScheme {
    // Correctly load the colors using colorResource(R.color.id)
    return darkColorScheme(
        primary = colorResource(R.color.primary_dark),
        secondary = colorResource(R.color.secondary_dark),
        tertiary = colorResource(R.color.accent_dark),
        background = Color(0xFF010104), // Fixed hexadecimal colors are fine
        surface = Color.Black,
        onSurface = colorResource(R.color.text_dark),
        onPrimary = colorResource(R.color.text_dark),
        onSecondary = colorResource(R.color.text_dark),
        onTertiary = colorResource(R.color.text_dark),
        onBackground = colorResource(R.color.text_dark),
        error = colorResource(R.color.danger), // Assuming R.color.danger is the same for both themes
    )
}

@Composable
private fun getLightColorScheme(darkTheme: Boolean): ColorScheme {
    // Correctly load the colors using colorResource(R.color.id)
    return lightColorScheme(
        primary = colorResource(R.color.primary_light),
        secondary = colorResource(R.color.secondary_light),
        tertiary = colorResource(R.color.accent_light),
        background = Color(0xFFFFFBFE),
        surface = Color.White,
        onPrimary = colorResource(R.color.text_dark), // Consider using a lighter color for onPrimary in light theme if needed
        onSecondary = colorResource(R.color.text_dark),
        onTertiary = colorResource(R.color.text_dark),
        onBackground = colorResource(R.color.text_light),
        onSurface = colorResource(R.color.text_light),
        error = colorResource(R.color.danger)
    )
}

@Composable
fun Heart2HeartTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {

    val DarkColorScheme: ColorScheme = getDarkColorScheme(darkTheme)
    val LightColorScheme: ColorScheme = getLightColorScheme(darkTheme)


    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}