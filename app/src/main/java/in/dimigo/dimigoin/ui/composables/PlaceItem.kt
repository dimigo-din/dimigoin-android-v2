package `in`.dimigo.dimigoin.ui.composables

import `in`.dimigo.dimigoin.R
import `in`.dimigo.dimigoin.domain.entity.place.Place
import `in`.dimigo.dimigoin.domain.entity.place.PlaceType
import `in`.dimigo.dimigoin.ui.composables.modifiers.noRippleClickable
import `in`.dimigo.dimigoin.ui.theme.*
import `in`.dimigo.dimigoin.ui.util.icon
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun PlaceItem(
    modifier: Modifier = Modifier,
    place: Place,
    isFavorite: Boolean,
    onFavoriteChange: (Boolean) -> Unit,
    isSelected: Boolean,
    onSelect: () -> Unit,
) = Row(
    modifier = modifier
        .wrapContentHeight()
        .fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically,
) {
    Icon(
        modifier = Modifier.size(24.dp),
        painter = painterResource(id = place.type.icon), contentDescription = null,
        tint = if (isSelected) Point else DTheme.colors.c3,
    )
    Spacer(modifier = Modifier.width(20.dp))
    Column(
        Modifier.weight(1f),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(
            text = place.description, style = DTheme.typography.t5, color = if (isSelected) LightPoint else DTheme.colors.c2,
        )
        Text(
            text = place.name, style = DTheme.typography.t4, color = if (isSelected) Point else DTheme.colors.c1,
        )
    }
    Spacer(modifier = Modifier.width(15.dp))
    Icon(
        modifier = Modifier
            .size(24.dp)
            .noRippleClickable { onFavoriteChange(!isFavorite) },
        painter = painterResource(id = R.drawable.ic_favorite), contentDescription = null,
        tint = if (isFavorite) YellowLight else DTheme.colors.c3.copy(alpha = 0.65f),
    )
    Spacer(modifier = Modifier.width(15.dp))
    Box(
        modifier = Modifier
            .size(55.dp, 33.dp)
            .clip(Shapes.small)
            .background(if (isSelected) Point else DTheme.colors.c4)
            .then(if (isSelected) Modifier else Modifier.clickable { onSelect() }),
    ) {
        Text(
            text = if (isSelected) "선택됨" else "선택",
            style = if (isSelected) DTheme.typography.t6 else DTheme.typography.t5,
            color = if (isSelected) Color.White else DTheme.colors.c1,
            modifier = Modifier.align(Alignment.Center),
        )
    }
}

// region Previews
@Preview(showBackground = true, backgroundColor = 0xFFFFFF)
@Composable
fun PlaceItemPreview1() {
    PlaceItem(
        place = TestPlace,
        isFavorite = false, onFavoriteChange = { },
        isSelected = false, onSelect = { },
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF)
@Composable
fun PlaceItemPreview2() {
    PlaceItem(
        place = TestPlace,
        isFavorite = false, onFavoriteChange = { },
        isSelected = true, onSelect = { },
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF)
@Composable
fun PlaceItemPreview3() {
    PlaceItem(
        place = TestPlace,
        isFavorite = true, onFavoriteChange = { },
        isSelected = false, onSelect = { },
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF)
@Composable
fun PlaceItemPreview4() {
    PlaceItem(
        place = TestPlace,
        isFavorite = true, onFavoriteChange = { },
        isSelected = true, onSelect = { },
    )
}

private val TestPlace = Place("", "1층 복도", "복도", "복도에 계신가요?", "본관", "1층", PlaceType.CORRIDOR)
// endregion
