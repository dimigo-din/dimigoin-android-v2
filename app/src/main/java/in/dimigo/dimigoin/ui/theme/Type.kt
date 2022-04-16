package `in`.dimigo.dimigoin.ui.theme

import `in`.dimigo.dimigoin.R
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val AppleSDGothicNeoFontFamily = FontFamily(
    Font(R.font.m, weight = FontWeight.Normal),
    Font(R.font.b, weight = FontWeight.Bold),
    Font(R.font.eb, weight = FontWeight.ExtraBold),
)

class DTypography {
    val t0 = TextStyle(
        fontFamily = AppleSDGothicNeoFontFamily,
        fontWeight = FontWeight.W700,
        fontSize = 26.sp,
        lineHeight = 31.2.sp,
    )
    val t1 = TextStyle(
        fontFamily = AppleSDGothicNeoFontFamily,
        fontWeight = FontWeight.W700,
        fontSize = 24.sp,
        lineHeight = 28.8.sp,
    )
    val t2 = TextStyle(
        fontFamily = AppleSDGothicNeoFontFamily,
        fontWeight = FontWeight.W700,
        fontSize = 22.sp,
        lineHeight = 26.4.sp,
    )
    val t3 = TextStyle(
        fontFamily = AppleSDGothicNeoFontFamily,
        fontWeight = FontWeight.W700,
        fontSize = 18.sp,
        lineHeight = 21.6.sp,
    )
    val t4 = TextStyle(
        fontFamily = AppleSDGothicNeoFontFamily,
        fontWeight = FontWeight.W700,
        fontSize = 16.sp,
        lineHeight = 19.2.sp,
    )
    val t5 = TextStyle(
        fontFamily = AppleSDGothicNeoFontFamily,
        fontWeight = FontWeight.W700,
        fontSize = 14.sp,
        lineHeight = 16.8.sp,
    )
    val t6 = TextStyle(
        fontFamily = AppleSDGothicNeoFontFamily,
        fontWeight = FontWeight.W700,
        fontSize = 12.sp,
        lineHeight = 14.4.sp,
    )
    val pageTitle = t1.copy(color = C2)
    val explainText = TextStyle(
        fontFamily = AppleSDGothicNeoFontFamily,
        fontWeight = FontWeight.W500,
        fontSize = 16.sp,
        lineHeight = 19.2.sp,
        color = C2,
    )
    val pageSubtitle = TextStyle(
        fontFamily = AppleSDGothicNeoFontFamily,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp,
        lineHeight = 16.8.sp,
        color = C2,
    )
    val mealMenu = TextStyle(
        fontFamily = AppleSDGothicNeoFontFamily,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp,
        lineHeight = 25.sp,
        color = C2,
    )
}
