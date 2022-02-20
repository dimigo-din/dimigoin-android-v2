package `in`.dimigo.dimigoin.ui.composables

import `in`.dimigo.dimigoin.R
import `in`.dimigo.dimigoin.domain.entity.Place
import `in`.dimigo.dimigoin.domain.entity.PlaceType
import `in`.dimigo.dimigoin.ui.composables.modifiers.noRippleClickable
import `in`.dimigo.dimigoin.ui.theme.C1
import `in`.dimigo.dimigoin.ui.theme.C2
import `in`.dimigo.dimigoin.ui.theme.C3
import `in`.dimigo.dimigoin.ui.theme.C4
import `in`.dimigo.dimigoin.ui.theme.DTypography
import `in`.dimigo.dimigoin.ui.theme.LightPoint
import `in`.dimigo.dimigoin.ui.theme.Point
import `in`.dimigo.dimigoin.ui.theme.Shapes
import `in`.dimigo.dimigoin.ui.theme.YellowLight
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
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
    val icon = when (place.type) {
        PlaceType.CLASSROOM -> R.drawable.ic_classroom
        PlaceType.RESTROOM -> R.drawable.ic_restroom
        PlaceType.CIRCLE, PlaceType.AFTERSCHOOL -> R.drawable.ic_circle_afterschool
        PlaceType.TEACHER -> R.drawable.ic_teacher_room
        PlaceType.CORRIDOR -> R.drawable.ic_corridor
        PlaceType.FARM -> R.drawable.ic_farm
        PlaceType.PLAYGROUND -> R.drawable.ic_playground
        PlaceType.GYM -> R.drawable.ic_gym
        PlaceType.LAUNDRY -> R.drawable.ic_laundry
        PlaceType.SCHOOL -> R.drawable.ic_school
        PlaceType.DORMITORY -> R.drawable.ic_dormitory
        PlaceType.ETC, PlaceType.ABSENT -> R.drawable.ic_idk_room
    }
    Icon(
        modifier = Modifier.size(24.dp),
        painter = painterResource(id = icon), contentDescription = null,
        tint = if (isSelected) Point else C3,
    )
    Spacer(modifier = Modifier.width(20.dp))
    Column(
        Modifier.weight(1f),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(
            text = place.description, style = DTypography.t5, color = if (isSelected) LightPoint else C2,
        )
        Text(
            text = place.name, style = DTypography.t4, color = if (isSelected) Point else C1,
        )
    }
    Spacer(modifier = Modifier.width(15.dp))
    Icon(
        modifier = Modifier
            .size(24.dp)
            .noRippleClickable { onFavoriteChange(!isFavorite) },
        painter = painterResource(id = R.drawable.ic_favorite), contentDescription = null,
        tint = if (isFavorite) YellowLight else C3.copy(alpha = 0.65f),
    )
    Spacer(modifier = Modifier.width(15.dp))
    Box(
        modifier = Modifier
            .size(55.dp, 33.dp)
            .clip(Shapes.small)
            .background(if (isSelected) Point else C4)
            .then(if (isSelected) Modifier else Modifier.clickable { onSelect() }),
    ) {
        Text(
            text = if (isSelected) "선택됨" else "선택",
            style = if (isSelected) DTypography.t6 else DTypography.t5,
            color = if (isSelected) Color.White else C1,
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
