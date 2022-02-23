package `in`.dimigo.dimigoin.ui.composables

import `in`.dimigo.dimigoin.R
import `in`.dimigo.dimigoin.domain.entity.place.PlaceCategory
import `in`.dimigo.dimigoin.ui.composables.modifiers.noRippleClickable
import `in`.dimigo.dimigoin.ui.theme.C1
import `in`.dimigo.dimigoin.ui.theme.C2
import `in`.dimigo.dimigoin.ui.theme.C3
import `in`.dimigo.dimigoin.ui.theme.DTypography
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun PlaceCategoryItem(
    modifier: Modifier = Modifier,
    category: PlaceCategory,
    @DrawableRes icon: Int,
    onClick: () -> Unit,
) = Row(
    modifier = modifier
        .noRippleClickable { onClick() }
        .wrapContentHeight()
        .fillMaxWidth(),
    horizontalArrangement = Arrangement.spacedBy(20.dp),
    verticalAlignment = Alignment.CenterVertically,
) {
    Icon(
        modifier = Modifier.size(24.dp),
        painter = painterResource(id = icon), contentDescription = null,
        tint = C3,
    )

    Column(
        Modifier.weight(1f),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(
            text = category.description, style = DTypography.t5, color = C2,
        )
        Text(
            text = category.name, style = DTypography.t4, color = C1,
        )
    }

    Icon(
        modifier = Modifier.size(7.dp, 12.dp),
        painter = painterResource(id = R.drawable.ic_arrow_right), contentDescription = null,
        tint = C3
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF)
@Composable
fun PlaceCategoryPreview() {
    PlaceCategoryItem(
        category = PlaceCategory("B1층", "급식실"),
        icon = R.drawable.ic_main,
        onClick = { },
    )
}
