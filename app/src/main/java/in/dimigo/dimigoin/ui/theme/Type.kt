package `in`.dimigo.dimigoin.ui.theme

import `in`.dimigo.dimigoin.R
import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val AppleSDGothicNeoFontFamily = FontFamily(
    Font(R.font.b, weight = FontWeight.Bold),
    Font(R.font.eb, weight = FontWeight.ExtraBold),
    Font(R.font.l, weight = FontWeight.Light),
    Font(R.font.m, weight = FontWeight.Medium)
)

val Typography = Typography(
    defaultFontFamily = AppleSDGothicNeoFontFamily,
    h1 = TextStyle(
        fontWeight = FontWeight.W700,
        fontSize = 24.sp,
        lineHeight = 4.8.sp,
    ),
    h2 = TextStyle(
        fontWeight = FontWeight.W700,
        fontSize = 22.sp,
        lineHeight = 4.4.sp,
    ),
    h3 = TextStyle(
        fontWeight = FontWeight.W700,
        fontSize = 20.sp,
        lineHeight = 4.sp,
    ),
    h4 = TextStyle(
        fontWeight = FontWeight.W700,
        fontSize = 18.sp,
        lineHeight = 3.6.sp,
    ),
    subtitle1 = TextStyle(
        fontWeight = FontWeight.W700,
        fontSize = 14.sp,
        lineHeight = 2.8.sp,
    ),
    body1 = TextStyle(
        fontWeight = FontWeight.W500,
        fontSize = 16.sp,
        lineHeight = 3.2.sp,
    ),
    body2 = TextStyle(
        fontWeight = FontWeight.W500,
        fontSize = 14.sp,
        lineHeight = 11.sp,
    ),
    button = TextStyle(
        fontWeight = FontWeight.W500,
        fontSize = 14.sp,
        lineHeight = 2.8.sp,
    ),
)