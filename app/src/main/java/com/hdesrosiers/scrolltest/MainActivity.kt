package com.hdesrosiers.scrolltest

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.rememberImagePainter
import com.hdesrosiers.scrolltest.ui.theme.ScrollTestTheme
import java.lang.Float.max
import kotlin.math.min

class MainActivity : ComponentActivity() {
  @RequiresApi(Build.VERSION_CODES.N)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      ScrollTestTheme {
        // A surface container using the 'background' color from the theme
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
          MonsterScreen("Android")
        }
      }
    }
  }
}

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun MonsterScreen(name: String) {
  val scrollState = rememberScrollState()
  val density = LocalDensity.current
  val configuration = LocalConfiguration.current
  val screenHeight = configuration.screenHeightDp.dp
  val screenWidth = configuration.screenWidthDp.dp
  val screenWidthPx = density.run { screenWidth.toPx() }

  ConstraintLayout(
    modifier = Modifier
      .padding(horizontal = 20.dp)
  ) {
    val (textRef, imageRef) = createRefs()

    Image(
      painter = rememberImagePainter(data = "https://www.cercatoridiatlantide.it/wp-content/uploads/2020/12/Goblin_PF2.png"),
      contentDescription = "Goblin",
      contentScale = ContentScale.FillHeight,
      modifier = Modifier
//        .background(color = Color.Green)
        .fillMaxHeight(0.5f)
        .constrainAs(imageRef) {
          start.linkTo(parent.start)
          end.linkTo(parent.end)
          top.linkTo(parent.top)
        }
        .graphicsLayer {
          alpha = min(1f, 1 - (scrollState.value / 900f))
          translationY = -scrollState.value * 0.5f
        }
    )
    Column(modifier = Modifier
//      .background(color = Color.Green)

      .width(density.run{ min(500f, screenWidthPx) }.dp)
      .constrainAs(textRef) {
        start.linkTo(parent.start)
        end.linkTo(parent.end)
        top.linkTo(parent.top)
      }
      .verticalScroll(scrollState)
      .padding(top = screenHeight * 0.5f, bottom = 40.dp)
    ) {
      Text(
        text = stringResource(id = R.string.lorem),
      )
    }

  }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
  ScrollTestTheme {
    MonsterScreen("Android")
  }
}