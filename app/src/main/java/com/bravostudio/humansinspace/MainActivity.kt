package com.bravostudio.humansinspace

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bravostudio.humansinspace.ui.theme.HumansInSpaceTheme
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        RetrofitHelper.init(this.application)

        setContent {
            HumansInSpaceTheme {
                MainScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun MainScreen(viewModel: MainViewModel = viewModel()) {
    val astronautCount = viewModel.astronautCount.value
    val astronautList = viewModel.astronautList
    viewModel.getAstronauts()

    val scope = rememberCoroutineScope()
    val modalBottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden, skipHalfExpanded = true)

    ModalBottomSheetLayout(
        sheetContent = { BottomSheetContent(astronautList) },
        sheetState = modalBottomSheetState,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Humans in Space",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        scope.launch {
                            modalBottomSheetState.show()
                        }
                    },
                    content = {
                        Icon(Icons.Filled.List, contentDescription = "list button")
                    }
                )
            }
        ) {
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .background(Color.Black)
            ) {

                Image(
                    painter = painterResource(id = R.drawable.background),
                    contentDescription = "background image",
                    contentScale = ContentScale.FillHeight
                )

                CreateEarth(modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = 150.dp)
                    .size(300.dp))

                CreateAstronaut(
                    // TODO RANDOMIZE OFFSET
                    modifier = Modifier.align(Alignment.CenterStart)
                )

                CreateAstronaut(
                    // TODO RANDOMIZE OFFSET
                    modifier = Modifier.align(Alignment.Center)
                )

                CreateAstronaut(
                    // TODO RANDOMIZE OFFSET
                    modifier = Modifier.align(Alignment.CenterEnd)
                )


                if (astronautCount > 0) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = 16.dp)
                            .border(
                                2.dp,
                                color = Color.White,
                                shape = RoundedCornerShape(10.dp)
                            )
                    ) {
                        Text(
                            text = "$astronautCount Humans",
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }

                }
            }
        }
    }
}

@Composable
fun CreateEarth(modifier: Modifier) {
    val infiniteTransition = rememberInfiniteTransition()

    val rotationEarth by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(40000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Image(
        painter = painterResource(id = R.drawable.earth),
        contentDescription = "earth image",
        modifier = modifier.rotate(rotationEarth)
    )
}

@Composable
fun CreateAstronaut(modifier: Modifier) {
    val infiniteTransition = rememberInfiniteTransition()

    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(Random.nextInt(2500, 3500)),
            repeatMode = RepeatMode.Reverse
        )
    )

    val rotationAstronaut by infiniteTransition.animateFloat(
        initialValue = -20f,
        targetValue = 20f,
        animationSpec = infiniteRepeatable(
            animation = tween(Random.nextInt(3500, 5000)),
            repeatMode = RepeatMode.Reverse
        )
    )

    val offsetY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 100f,
        animationSpec = infiniteRepeatable(
            animation = tween(Random.nextInt(5000, 10000)),
            repeatMode = RepeatMode.Reverse
        )
    )

    Image(
        painter = painterResource(id = R.drawable.astronaut),
        contentDescription = "astronaut image",
        modifier = modifier
            .scale(scale)
            .rotate(rotationAstronaut)
            .absoluteOffset(y = offsetY.roundToInt().dp)
    )
}

@Composable
private fun BottomSheetContent(astronautList: MutableList<Astronaut>) {
    Column(modifier = Modifier.padding(8.dp)) {
        Text(text = "List of Humans", fontSize = 18.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        Divider(modifier = Modifier.padding(8.dp))
        astronautList.forEachIndexed { i, ast ->
            BottomSheetListItem(numberText = i + 1, name = ast.name, craft = ast.craft)
        }
    }
}

@Composable
private fun BottomSheetListItem(numberText: Int, name: String, craft: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "" + numberText)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = name)
        Text(text = craft, modifier = Modifier.weight(1f), textAlign = TextAlign.End)
    }
}