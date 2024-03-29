package `in`.dimigo.dimigoin.ui.screen

import `in`.dimigo.dimigoin.R
import `in`.dimigo.dimigoin.ui.theme.DTheme
import `in`.dimigo.dimigoin.ui.theme.Point
import `in`.dimigo.dimigoin.ui.theme.Shapes
import `in`.dimigo.dimigoin.viewmodel.MyInfoViewModel
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import com.google.accompanist.pager.ExperimentalPagerApi
import com.skydoves.landscapist.glide.GlideImage
import org.koin.androidx.compose.getViewModel
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MyInfoScreen(
    myInfoViewModel: MyInfoViewModel = getViewModel(),
) {
    val context = LocalContext.current
    Surface(
        Modifier
            .fillMaxHeight()
    ) {
        var visible by remember { mutableStateOf(false) }

        Column(
            Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(top = 36.dp, bottom = 120.dp)
        ) {
            Column(
                Modifier
                    .padding(horizontal = 20.dp)
            ) {
                Text(
                    modifier = Modifier.padding(start = 15.dp),
                    text = "", style = DTheme.typography.t5, color = DTheme.colors.c2
                )
                Spacer(Modifier.height(5.dp))
                Text(
                    modifier = Modifier.padding(start = 15.dp),
                    text = NavScreen.MyInfo.name, style = DTheme.typography.t0
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
                        .align(Alignment.CenterHorizontally)
                        .clickable {
                            myInfoViewModel.runWhenAuthenticated(
                                onSucceeded = { visible = true },
                                context = context
                            )
                        },
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
                                .padding(vertical = 7.dp)
                                .constrainAs(touchText) {
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                    top.linkTo(kdmhsIcon.bottom, margin = 120.dp)
                                    bottom.linkTo(parent.bottom)
                                },
                            textAlign = TextAlign.Center,
                            text = "터치하여 모바일 학생증 열기",
                            style = DTheme.typography.pageSubtitle.copy(fontSize = 11.sp),
                            fontWeight = Bold,
                            color = Point,
                        )
                    }
                }
            }
        }
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(initialAlpha = 0.3f),
            exit = fadeOut()
        ) {
            myInfoViewModel.myIdentity?.let {
                Dialog(onDismissRequest = { visible = false}) {
                    StudentCard(
                        modifier = Modifier
                            .size(320.dp, 500.dp),
                        name = it.name,
                        birth = it.birthdate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                        grade = it.grade,
                        `class` = it.`class`,
                        number = it.number
                    )
                }
            }
        }
    }
}

@Preview(widthDp = 320, heightDp = 500)
@Composable
fun MyInfoPrev() {
    StudentCard(
        name = "이준호",
        birth = "2004-04-21",
        grade = 3,
        `class` = 4,
        number = 23
    )
}

@Composable
fun StudentCard(
    modifier: Modifier = Modifier,
    viewModel: MyInfoViewModel = getViewModel(),
    name: String,
    birth: String,
    grade: Int,
    `class`: Int,
    number: Int,
    gwa: String = when (`class`) {
        1 -> "e-비즈니스과"
        2 -> "디지털컨텐츠과"
        3, 4 -> "웹프로그래밍과"
        5, 6 -> "해킹방어과"
        else -> ""
    },
) {
    Card(
        modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
    ) {
        Column(
            Modifier.fillMaxWidth()
                .background(Color.White)
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .background(Point)
                    .padding(top = 20.dp, start = 25.dp, bottom = 106.dp)
            ) {
                Column(
                    Modifier
                ) {
                    Row(
                        Modifier,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            modifier = Modifier,
                            painter = painterResource(id = R.drawable.ic_student_card),
                            contentDescription = null,
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "모바일 학생증",
                            style = DTheme.typography.pageSubtitle,
                            fontWeight = Bold,
                            color = Color.White,
                        )
                    }
                    Spacer(modifier = Modifier.height(25.dp))
                    Row(
                        Modifier
                            .padding(start = 7.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        GlideImage(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color.White)
                                .size(105.dp, 140.dp),
                            imageModel = viewModel.myIdentity?.photos?.get(0),
                            contentScale = ContentScale.Crop,
                        )
                        Spacer(modifier = Modifier.width(30.dp))
                        Column {
                            Text(
                                text = name,
                                style = DTheme.typography.pageSubtitle.copy(fontSize = 22.sp),
                                fontWeight = Bold,
                                color = Color.White,
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(
                                text = birth,
                                style = DTheme.typography.pageSubtitle.copy(fontSize = 16.sp),
                                fontWeight = Bold,
                                color = Color.White,
                            )
                            Spacer(modifier = Modifier.height(15.dp))
                            Text(
                                text = gwa,
                                style = DTheme.typography.pageSubtitle,
                                fontWeight = Bold,
                                color = Color.White,
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "${grade}학년 ${`class`}반 ${number}번",
                                style = DTheme.typography.pageSubtitle,
                                fontWeight = Bold,
                                color = Color.White,
                            )
                        }
                    }
                }

            }
            Box(
                Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 62.dp, bottom = 35.dp)
            ) {
                Column {
                    Icon(
                        modifier = Modifier,
                        painter = painterResource(id = R.drawable.ic_kdmhs),
                        contentDescription = null,
                        tint = Color.Black
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    viewModel.renderBarcode(width = 200, height = 40)?.asImageBitmap()
                        ?.let {
                            Image(
                                modifier = Modifier
                                    .size(200.dp, 40.dp),
                                bitmap = it,
                                contentDescription = null
                            )
                        }
                }
            }
        }
    }
}