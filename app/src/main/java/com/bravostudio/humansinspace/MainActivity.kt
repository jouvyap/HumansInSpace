package com.bravostudio.humansinspace

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bravostudio.humansinspace.ui.theme.HumansInSpaceTheme
import kotlinx.coroutines.launch
import java.lang.Math.random
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
    val infiniteTransitionScaAst = rememberInfiniteTransition()
    val infiniteTransitionRotAst = rememberInfiniteTransition()
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

                CreateEarth(modifier = Modifier
                    .align(Alignment.Center)
                    .size(300.dp))

                CreateAstronaut(modifier = Modifier.align(Alignment.TopCenter))
                CreateAstronaut(modifier = Modifier.align(Alignment.TopStart))
                CreateAstronaut(modifier = Modifier.align(Alignment.TopEnd))


                if (astronautCount > 0) {
                    Text(
                        text = "" + astronautCount,
                        fontSize = 48.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        modifier = Modifier.align(Alignment.Center)
                    )
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
            animation = tween(30000, easing = LinearEasing),
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
    val infiniteTransitionScale = rememberInfiniteTransition()
    val infiniteTransitionRotation = rememberInfiniteTransition()
    val scale by infiniteTransitionScale.animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(Random.nextInt(2000, 4000)),
            repeatMode = RepeatMode.Reverse
        )
    )

    val rotationAstronaut by infiniteTransitionRotation.animateFloat(
        initialValue = -20f,
        targetValue = 20f,
        animationSpec = infiniteRepeatable(
            animation = tween(Random.nextInt(3000, 5000)),
            repeatMode = RepeatMode.Reverse
        )
    )

    Image(
        painter = painterResource(id = R.drawable.astronaut),
        contentDescription = "astronaut image",
        modifier = modifier
            .scale(scale)
            .rotate(rotationAstronaut)
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