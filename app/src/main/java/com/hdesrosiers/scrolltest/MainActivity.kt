package com.hdesrosiers.scrolltest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.rememberImagePainter
import com.hdesrosiers.scrolltest.ui.theme.ScrollTestTheme
import kotlin.math.min

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      ScrollTestTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
          MonsterScreen("Goblin")
        }
      }
    }
  }
}

@Composable
fun MonsterScreen(name: String) {
  val scrollState = rememberScrollState()
  val density = LocalDensity.current
  val configuration = LocalConfiguration.current
  val screenHeight = configuration.screenHeightDp.dp
  val screenWidth = configuration.screenWidthDp.dp
  val screenWidthPx = density.run { screenWidth.toPx() }
  val screenHeightPx = density.run { screenHeight.toPx() }
  val scrollTop = screenHeightPx * 0.5f
  val titleHeight = remember { mutableStateOf(0) }
  val scrollTopWithTitle = scrollTop - titleHeight.value

  ConstraintLayout(
    modifier = Modifier
      .padding(horizontal = 20.dp)
  ) {
    val (descriptionRef, imageRef, titleRef) = createRefs()

    Text(
      text = name,
      style = MaterialTheme.typography.h3.copy(fontWeight = FontWeight.Black),
      modifier = Modifier
        .fillMaxWidth()
        .background(
          color = if (scrollState.value * 1f < scrollTopWithTitle)
            Color.Transparent else MaterialTheme.colors.surface
        )
        .onGloballyPositioned { coordinates ->
          coordinates.size
          titleHeight.value = coordinates.size.height
        }
        .zIndex(1f)
        .constrainAs(titleRef) {
          start.linkTo(descriptionRef.start)
        }

    )

    Image(
      painter = rememberImagePainter(data = "https://www.cercatoridiatlantide.it/wp-content/uploads/2020/12/Goblin_PF2.png"),
      contentDescription = "Goblin",
      contentScale = ContentScale.FillHeight,
      modifier = Modifier
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

      .width(density.run { min(500f, screenWidthPx) }.dp)
      .constrainAs(descriptionRef) {
        start.linkTo(parent.start)
        end.linkTo(parent.end)
        top.linkTo(parent.top)
      }
      .verticalScroll(scrollState)
      .padding(top = screenHeight * 0.5f, bottom = 40.dp)
    ) {
      Column(
        modifier = Modifier
//          .offset(y = density.run { min(1000f, scrollState.value * 1f) }.dp)
//          .offset(y = (scrollState.value).dp)
          .graphicsLayer(
            translationY = if (scrollState.value * 1f < scrollTopWithTitle)
              0f else scrollState.value * 1f - scrollTopWithTitle
          )
//          .graphicsLayer(translationY = scrollState.value * 1f)
//          .graphicsLayer(translationY = -1f * scrollState.value)
//          .graphicsLayer(translationY = min(1000f, scrollState.value * -1f))
          .zIndex(1f)
      ) {
        Divider(
          thickness = 3.dp,
          color = MaterialTheme.colors.secondary
        )
        VitalStatsBlock(
          modifier = Modifier
            .fillMaxWidth()
            .background(
              color = if (scrollState.value * 1f < scrollTopWithTitle)
                Color.Transparent else MaterialTheme.colors.surface
            )
        )
        Divider(
          thickness = 3.dp,
          color = MaterialTheme.colors.secondary
        )
        Spacer(
          modifier = Modifier
            .fillMaxWidth()
            .height(20.dp)
            .background(
              brush = if (scrollState.value * 1f < scrollTop)
                Brush.verticalGradient(
                  colors = listOf(Color.Transparent, Color.Transparent),
                ) else
                Brush.verticalGradient(
                  colors = listOf(MaterialTheme.colors.surface, Color.Transparent),
                )
            )
        )
      }


      Text(
        text = stringResource(id = R.string.lorem),
      )
    }

  }
}

@Composable
fun VitalStatsBlock(modifier: Modifier = Modifier) {

  Row(
    modifier = modifier
      .padding(vertical = 10.dp)
  ) {
    VitalStat()
    VitalStat()
    VitalStat()
  }
}

@Composable
fun VitalStat() {
  Row(
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Icon(
      imageVector = Icons.Filled.Favorite,
      contentDescription = "Favorite",
      tint = Color.Red,
      modifier = Modifier
        .size(18.dp)
    )
    Spacer(modifier = Modifier.width(3.dp))
    Text(
      text = "HP",
      style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Black),
      modifier = Modifier
        .padding(end = 6.dp)
    )
    Text(
      text = "7",
      style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Light),
      modifier = Modifier
        .padding(end = 12.dp)
    )
  }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
  ScrollTestTheme {
    MonsterScreen("Android")
  }
}