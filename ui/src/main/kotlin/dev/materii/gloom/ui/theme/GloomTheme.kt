package dev.materii.gloom.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import dev.materii.gloom.util.supportsMonet

@Composable
fun GloomTheme(
    darkTheme: Boolean,
    dynamicColor: Boolean,
    content: @Composable () -> Unit
) {
    val (colors, gloomColors) = getColorSchemes(darkTheme, dynamicColor)

    CompositionLocalProvider(
        LocalGloomColorScheme provides gloomColors
    ) {
        MaterialTheme(
            colorScheme = colors,
            typography = Typography(),
            shapes = Shapes(),
            content = content
        )
    }
}

/**
 * Retrieves the color schemes to be used based on user settings
 *
 * @param darkTheme Whether or not to use the dark theme variant
 * @param dynamicColor (Android 12+ only) Whether or not to use a dynamic color scheme
 */
@Composable
fun getColorSchemes(darkTheme: Boolean, dynamicColor: Boolean): Pair<ColorScheme, GloomColorScheme> {
    // We don't technically need to check for dynamic theming support
    // here because its locked behind a setting that itself is SDK restricted
    // but its good to be cautious anyways.
    return when {
        dynamicColor && darkTheme && supportsMonet -> dynamicDarkColorScheme(LocalContext.current) to darkGloomColorScheme()
        dynamicColor && !darkTheme && supportsMonet -> dynamicLightColorScheme(LocalContext.current) to lightGloomColorScheme()
        darkTheme -> darkColorScheme() to darkGloomColorScheme()
        else -> lightColorScheme() to lightGloomColorScheme()
    }
}