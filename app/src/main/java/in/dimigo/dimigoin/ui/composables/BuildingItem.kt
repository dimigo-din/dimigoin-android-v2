package `in`.dimigo.dimigoin.ui.composables

import `in`.dimigo.dimigoin.R
import `in`.dimigo.dimigoin.domain.entity.place.Building
import `in`.dimigo.dimigoin.ui.theme.C1
import `in`.dimigo.dimigoin.ui.theme.C2
import `in`.dimigo.dimigoin.ui.theme.DTypography
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
    @DrawableRes image: Int? = getBuildingImage(building),
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
            text = building.type, style = DTypography.t5, color = C2,
            modifier = Modifier.constrainAs(typeText) {
                top.linkTo(parent.top, 25.dp)
                start.linkTo(parent.start, 25.dp)
            }
        )
        Text(
            text = building.name, style = DTypography.t4, color = C1,
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
        building = Building(type = "학교", name = "본관",  listOf()),
        onClick = { }
    )
}

fun getBuildingImage(building: Building): Int? {
    return when (building.name) {
        "본관" -> R.drawable.bongwan
        "신관" -> R.drawable.singwan
        "학봉관" -> R.drawable.hakbonggwan
        "우정학사" -> R.drawable.woojeonghaksa
        "기타 장소" -> R.drawable.cheyuggwan
        else -> null
    }
}