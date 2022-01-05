package `in`.dimigo.dimigoin.ui.composables

import `in`.dimigo.dimigoin.domain.entity.Building
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BuildingList(
    modifier: Modifier = Modifier,
    buildings: List<Building>,
    onBuildingClick: (Building) -> Unit,
) = LazyRow(
    modifier = modifier,
    horizontalArrangement = Arrangement.spacedBy(15.dp)
) {
    item { Spacer(Modifier.width(5.dp)) }
    items(buildings) { building ->
        BuildingItem(
            building = building,
            onClick = { onBuildingClick(building) }
        )
    }
    item { Spacer(Modifier.width(5.dp)) }
}
