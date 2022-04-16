package `in`.dimigo.dimigoin.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

typealias DTheme = DimigoinTheme

object DimigoinTheme {
    val colors: DColors
        @Composable
        @ReadOnlyComposable
        get() = LocalColors.current

    val typography: DTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalTypography.current
}

@Composable
fun DimigoinTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    colors: DColors = if (darkTheme) darkDimigoinColors() else lightDimigoinColors(),
    typography: DTypography = DTypography(),
    shapes: Shapes = MaterialTheme.shapes,
    content: @Composable () -> Unit
) {
    val rememberedColors = remember {
        // Explicitly creating a new object here so we don't mutate the initial [colors]
        // provided, and overwrite the values set in it.
        colors.copy()
    }.apply { updateColorsFrom(colors) }

    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(Color.Transparent)
    systemUiController.statusBarDarkContentEnabled = !darkTheme

    CompositionLocalProvider(
        LocalColors provides rememberedColors,
        LocalTypography provides typography,
    ) {
        val materialColors = if (darkTheme) {
            DarkColorPalette
        } else {
            LightColorPalette
        }
        MaterialTheme(materialColors) {
            ProvideTextStyle(value = typography.t4, content)
        }
    }
}

private val DarkColorPalette = darkColors(
    primary = Point,
    surface = Color(0xFF222222),
    onPrimary = Color.White,
    error = Red,
)

private val LightColorPalette = lightColors(
    primary = Point,
    background = C4,
    onBackground = C2,
    surface = Color.White,
    onPrimary = Color.White,
    onSurface = Color.Black,
    error = Red,
)

internal val LocalTypography = staticCompositionLocalOf { DTypography() }
internal val LocalColors = staticCompositionLocalOf { lightDimigoinColors() }

fun lightDimigoinColors() = DColors(
    surface = Color.White,
    onSurface = Color.Black,
    c0 = C0,
    c1 = C1,
    c2 = C2,
    c3 = C3,
    c4 = C4,
    error = Red,
    point = Point,
    pointVariant = LightPoint,
    yellow = YellowLight,
)

fun darkDimigoinColors() = DColors(
    surface = Color.Black,
    onSurface = Color.White,
    c0 = C4,
    c1 = C3,
    c2 = C2,
    c3 = C1,
    c4 = C0,
    error = Red,
    point = Point,
    pointVariant = C3,
    yellow = YellowLight,
)

class DColors(
    surface: Color,
    onSurface: Color,
    c0: Color,
    c1: Color,
    c2: Color,
    c3: Color,
    c4: Color,
    point: Color,
    pointVariant: Color,
    error: Color,
    yellow: Color,
) {
    var surface by mutableStateOf(surface, structuralEqualityPolicy())
        private set
    var onSurface by mutableStateOf(onSurface, structuralEqualityPolicy())
        private set
    var c0 by mutableStateOf(c0, structuralEqualityPolicy())
        private set
    var c1 by mutableStateOf(c1, structuralEqualityPolicy())
        private set
    var c2 by mutableStateOf(c2, structuralEqualityPolicy())
        private set
    var c3 by mutableStateOf(c3, structuralEqualityPolicy())
        private set
    var c4 by mutableStateOf(c4, structuralEqualityPolicy())
        private set
    var point by mutableStateOf(point, structuralEqualityPolicy())
        private set
    var pointVariant by mutableStateOf(pointVariant, structuralEqualityPolicy())
        private set
    var error by mutableStateOf(error, structuralEqualityPolicy())
        private set
    var yellow by mutableStateOf(yellow, structuralEqualityPolicy())
        private set

    fun copy(
        surface: Color = this.surface,
        onSurface: Color = this.onSurface,
        c0: Color = this.c0,
        c1: Color = this.c1,
        c2: Color = this.c2,
        c3: Color = this.c3,
        c4: Color = this.c4,
        point: Color = this.point,
        pointVariant: Color = this.pointVariant,
        error: Color = this.error,
        yellow: Color = this.yellow,
    ) = DColors(
        surface,
        onSurface,
        c0,
        c1,
        c2,
        c3,
        c4,
        point,
        pointVariant,
        error,
        yellow,
    )

    fun updateColorsFrom(other: DColors) {
        surface = other.surface
        onSurface = other.onSurface
        c0 = other.c0
        c1 = other.c1
        c2 = other.c2
        c3 = other.c3
        c4 = other.c4
        point = other.point
        pointVariant = other.pointVariant
        error = other.error
        yellow = other.yellow
    }
}