package `in`.dimigo.dimigoin.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorPalette = darkColors(
    primary = Point,
    surface = Color(0xFF222222),
    onPrimary = Color.White,
    error = Red,
)

private val LightColorPalette = lightColors(
    primary = Point,
    background = Grey250,
    onBackground = C2,
    surface = Color.White,
    onPrimary = Color.White,
    onSurface = Color.Black,
    error = Red,
)

@Composable
fun DimigoinTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(Color.Transparent)

    MaterialTheme(
        colors = colors,
        shapes = Shapes,
        content = content
    )
}
