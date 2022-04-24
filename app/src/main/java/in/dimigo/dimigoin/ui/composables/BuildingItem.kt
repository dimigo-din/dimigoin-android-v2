package `in`.dimigo.dimigoin.ui.composables

import `in`.dimigo.dimigoin.R
import `in`.dimigo.dimigoin.domain.entity.place.Building
import `in`.dimigo.dimigoin.domain.entity.place.BuildingType
import `in`.dimigo.dimigoin.ui.theme.DTheme
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout

@Composable
fun BuildingItem(
    modifier: Modifier = Modifier,
    building: Building,
    @DrawableRes image: Int? = getBuildingImage(building.type),
    onClick: () -> Unit,
) = Surface(
    modifier
        .size(130.dp, 160.dp)
        .clip(RoundedCornerShape(15.dp))
        .clickable { onClick() }
) {
    ConstraintLayout {
        val (typeText, nameText, imageRef) = createRefs()

        Text(
            text = building.type.category.value, style = DTheme.typography.t5, color = DTheme.colors.c2,
            modifier = Modifier.constrainAs(typeText) {
                top.linkTo(parent.top, 25.dp)
                start.linkTo(parent.start, 25.dp)
            }
        )
        Text(
            text = building.type.value, style = DTheme.typography.t4, color = DTheme.colors.c1,
            modifier = Modifier.constrainAs(nameText) {
                top.linkTo(typeText.bottom, 5.dp)
                start.linkTo(typeText.start)
            }
        )
        image?.let {
            Image(
                painter = painterResource(id = it), contentDescription = null,
                modifier = Modifier.constrainAs(imageRef) {
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom, 15.dp)
                }
                    .width(115.dp),
            )
        }
    }
}

@Preview
@Composable
fun BuildingItemPreview() {
    BuildingItem(
        building = Building(type = BuildingType.MAIN, listOf()),
        onClick = { }
    )
}

fun getBuildingImage(building: BuildingType): Int {
    return when (building) {
        BuildingType.MAIN -> R.drawable.bongwan
        BuildingType.NEWBUILDING -> R.drawable.singwan
        BuildingType.HAKBONG -> R.drawable.hakbonggwan
        BuildingType.UJEONG -> R.drawable.woojeonghaksa
        BuildingType.ETC -> R.drawable.cheyuggwan
    }
}