package com.bravostudio.humansinspace

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
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
    val infiniteTransitionRotEar = rememberInfiniteTransition()
    val modalBottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

    val scale by infiniteTransitionScaAst.animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000),
            repeatMode = RepeatMode.Reverse
        )
    )

    val rotationAstronaut by infiniteTransitionRotAst.animateFloat(
        initialValue = -20f,
        targetValue = 20f,
        animationSpec = infiniteRepeatable(
            animation = tween(5000),
            repeatMode = RepeatMode.Reverse
        )
    )

    val rotationEarth by infiniteTransitionRotEar.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

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
                .padding(it)) {
                Image(
                    painter = painterResource(id = R.drawable.earth),
                    contentDescription = "earth image",
                    modifier = Modifier
                        .rotate(rotationEarth)
                        .align(Alignment.Center)
                        .size(300.dp)
                )

                Image(
                    painter = painterResource(id = R.drawable.astronaut),
                    contentDescription = "astronaut image",
                    modifier = Modifier
                        .scale(scale)
                        .rotate(rotationAstronaut)
                        .align(Alignment.TopEnd)
                )

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
private fun BottomSheetContent(astronautList: MutableList<Astronaut>) {
    Column(modifier = Modifier.padding(8.dp)) {
        Text(text = "List of Humans", fontSize = 18.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        Divider(modifier = Modifier.padding(8.dp))
        astronautList.forEachIndexed { _, ast ->
            Text(text = ast.name + " - " + ast.craft)
        }
    }
}