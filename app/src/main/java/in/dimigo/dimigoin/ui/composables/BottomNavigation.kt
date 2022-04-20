package `in`.dimigo.dimigoin.ui.composables

import `in`.dimigo.dimigoin.ui.theme.DTheme
import `in`.dimigo.dimigoin.ui.theme.Point
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun BottomNavigation(
    content: @Composable RowScope.() -> Unit,
) {
    if (!isSystemInDarkTheme()) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(30.dp)
                .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.Transparent,
                            Color(0f, 0f, 0f, 0.05f)
                        )
                    )
                )
        )
    }
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
            .height(60.dp)
            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)),
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            content()
        }
    }
}

@Composable
fun RowScope.BottomNavigationItem(
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit,
    label: @Composable () -> Unit,
    selected: Boolean,
    onClick: () -> Unit,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    selectedColor: Color = Point,
    unSelectedColor: Color = DTheme.colors.c3,
) {
    val color = if (selected) selectedColor else unSelectedColor
    val animatedColor = animateColorAsState(targetValue = color)
    val ripple = rememberRipple(bounded = false, color = color)

    Column(
        modifier = modifier
            .weight(1f)
            .selectable(
                selected,
                role = Role.Tab,
                onClick = onClick,
                interactionSource = interactionSource,
                indication = ripple,
                enabled = true
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        CompositionLocalProvider(LocalContentColor provides animatedColor.value) {
            icon()
            label()
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF, name = "Bottom Navigation Bar Item (not selected)")
@Composable
fun BNItemPreview() {
    Row {
        BottomNavigationItem(
            icon = { Icon(Icons.Filled.Favorite, contentDescription = null) },
            label = { Text("Favorite", style = DTheme.typography.t6) },
            selected = false,
            onClick = { },
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF, name = "Bottom Navigation Bar Item (not selected)")
@Composable
fun BNItemPreviewSelected() {
    Row {
        BottomNavigationItem(
            icon = { Icon(Icons.Filled.Favorite, contentDescription = null, modifier = Modifier.size(24.dp)) },
            label = { Text("Favorite", style = DTheme.typography.t6) },
            selected = true,
            onClick = { },
        )
    }
}

@Preview(showBackground = false, name = "Bottom Navigation Bar")
@Composable
fun BottomNavigationPreview() {
    BottomNavigation {
        listOf("메인", "급식", "일정", "신청", "학생증").forEach {
            BottomNavigationItem(
                icon = { Icon(Icons.Filled.Favorite, contentDescription = null) },
                label = { Text(it, style = DTheme.typography.t6) },
                selected = it == "급식",
                onClick = { },
            )
        }
    }
}