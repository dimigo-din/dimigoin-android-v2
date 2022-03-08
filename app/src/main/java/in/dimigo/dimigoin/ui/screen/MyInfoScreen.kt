package `in`.dimigo.dimigoin.ui.screen

import `in`.dimigo.dimigoin.R
import `in`.dimigo.dimigoin.ui.theme.C2
import `in`.dimigo.dimigoin.ui.theme.C3
import `in`.dimigo.dimigoin.ui.theme.DTypography
import `in`.dimigo.dimigoin.ui.theme.Point
import `in`.dimigo.dimigoin.ui.theme.Shapes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.google.accompanist.pager.ExperimentalPagerApi

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MyInfoScreen(
    gridPadding: Dp = 10.dp,
) {
    Surface(
        Modifier
            .fillMaxHeight()
            .statusBarsPadding()
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(top = 36.dp)
        ) {
            Column(
                Modifier
                    .padding(horizontal = 20.dp)
            ) {
                Text(
                    modifier = Modifier.padding(start = 15.dp),
                    text = "", style = DTypography.t5, color = C2
                )
                Spacer(Modifier.height(5.dp))
                Text(
                    modifier = Modifier.padding(start = 15.dp),
                    text = Screen.MyInfo.name, style = DTypography.t0
                )
            }
            Column(
                Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {
                Card(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 35.dp)
                        .align(Alignment.CenterHorizontally),
                    shape = RoundedCornerShape(10.dp),
                ) {
                    ConstraintLayout(
                        Modifier
                            .fillMaxWidth()
                            .background(Point)
                            .padding(horizontal = 19.dp, vertical = 17.dp)
                    ) {
                        val (kdmhsIcon, arrowButton, touchText) = createRefs()
                        Icon(
                            modifier = Modifier
                                .constrainAs(kdmhsIcon) {
                                    top.linkTo(parent.top)
                                    start.linkTo(parent.start)
                                }
                                .size((200 / 1.5).dp, (29 / 1.5).dp),
                            painter = painterResource(id = R.drawable.ic_kdmhs),
                            contentDescription = null,
                            tint = Color.White
                        )
                        Icon(
                            modifier = Modifier
                                .constrainAs(arrowButton) {
                                    top.linkTo(kdmhsIcon.top)
                                    bottom.linkTo(kdmhsIcon.bottom)
                                    end.linkTo(parent.end)
                                },
                            painter = painterResource(id = R.drawable.ic_arrow_right),
                            contentDescription = null,
                            tint = Color.White
                        )
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White, shape = Shapes.medium)
                                .padding(vertical = 5.dp)
                                .constrainAs(touchText) {
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                    top.linkTo(kdmhsIcon.bottom, margin = 120.dp)
                                    bottom.linkTo(parent.bottom)
                                },
                            textAlign = TextAlign.Center,
                            text = "터치하여 모바일 학생증 열기",
                            style = DTypography.pageSubtitle.copy(fontSize = 11.sp),
                            fontWeight = Bold,
                            color = Point,
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Card(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 35.dp)
                        .align(Alignment.CenterHorizontally),
                    shape = RoundedCornerShape(10.dp),
                ) {
                    ConstraintLayout(
                        Modifier
                            .fillMaxWidth()
                            .background(C3)
                            .padding(horizontal = 19.dp, vertical = 14.dp)
                    ) {
                        val (text, arrowButton) = createRefs()
                        Row(
                            Modifier.constrainAs(text) {
                                top.linkTo(parent.top)
                                bottom.linkTo(parent.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_information),
                                contentDescription = null,
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                textAlign = TextAlign.Center,
                                text = "사용 전 다음 내용을 반드시 읽어주세요",
                                style = DTypography.pageSubtitle.copy(fontSize = 11.sp),
                                fontWeight = Bold,
                                color = Color.White,
                            )
                        }
                        Icon(
                            modifier = Modifier
                                .constrainAs(arrowButton) {
                                    top.linkTo(text.top)
                                    bottom.linkTo(text.bottom)
                                    end.linkTo(parent.end)
                                },
                            painter = painterResource(id = R.drawable.ic_arrow_right),
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}
