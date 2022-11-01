package com.serverdriven.app

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.graphics.Color.parseColor
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.serverdriven.app.ui.home.CustomModifier
import com.serverdriven.app.ui.home.CustomView
import com.serverdriven.app.ui.home.HomeViewModel
import com.serverdriven.app.ui.home.TypeName
import com.serverdriven.app.ui.theme.ServerDrivenAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ServerDrivenAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    HomeScreen()
                }
            }
        }
    }
}

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel()
) {
    val context = LocalContext.current
    val data by viewModel.dataState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getData(context)
    }

    data.error?.let {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Something went wrong! ${it}", color = Color.Red)
        }
    }

    AnimatedVisibility(visible = data.isLoading) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator()
        }
    }

    AnimatedVisibility(visible = data.isLoading.not()) {
        SetUpView(children = data.child)
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            viewModel.changeNextDesign(context)
        }) {
            Text(text = "Next Design")
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Design from service ${viewModel.fileName.collectAsState().value}",
        modifier = Modifier.padding(top = 16.dp))
    }
}


@Composable
fun SetUpView(children: List<CustomView>) {
    children.forEach { child ->
        when (child.typeName) {
            TypeName.UNKNOWN -> {
                Spacer(modifier = Modifier.requiredSize(0.dp))
            }
            TypeName.CARD -> {
                Card(modifier = Modifier.fromProps(child.modifier)) {
                    SetUpView(children = child.children)
                }
            }
            TypeName.COLUMN -> {
                Column(
                    modifier = Modifier.fromProps(child.modifier),
                    verticalArrangement = child.getVerticalColArrangement(),
                    horizontalAlignment = child.getHorizontalColArrangement()
                ) {
                    SetUpView(children = child.children)
                }
            }
            TypeName.ROW -> {
                Row(
                    modifier = Modifier.fromProps(child.modifier),
                    horizontalArrangement = child.getHorizontalRowArrangement(),
                    verticalAlignment = child.getVerticalRowArrangement()
                ) {
                    SetUpView(children = child.children)
                }
            }
            TypeName.TEXT -> {
                Text(
                    text = child.value.orEmpty(),
                    style = child.modifier.getTextStyle(),
                    modifier = Modifier.fromProps(child.modifier)
                )
            }
            TypeName.IMAGE -> {
                val source =
                    rememberAsyncImagePainter(child.value, contentScale = ContentScale.Crop)
                Image(
                    painter = source,
                    contentDescription = null,
                    modifier = Modifier.fromProps(child.modifier)
                )
            }
            TypeName.ICON -> {
                Icon(
                    child.getIcon(),
                    contentDescription = null,
                    modifier = Modifier.fromProps(child.modifier),
                    tint = child.getColor()
                )
            }
        }
    }
}
fun CustomView.getColor(): Color {
    return  Color(parseColor(modifier.color))
}

fun CustomView.getIcon(): ImageVector {
    return when(value) {
        "locationOn" -> Icons.Default.LocationOn
        else -> Icons.Default.LocationOn
    }
}


fun CustomView.getVerticalRowArrangement(): Alignment.Vertical {
    if (verticalAlignment.defaultIfNull() != "default") {
        return when (verticalAlignment.defaultIfNull()) {
            "CenterVertically" -> Alignment.CenterVertically
            "Top" -> Alignment.Top
            "Bottom" -> Alignment.Bottom
            else -> Alignment.Top
        }
    }
    return Alignment.Top
}

fun CustomView.getHorizontalRowArrangement(): Arrangement.Horizontal {
    if (horizontalArrangement.defaultIfNull() != "default") {
        return when (horizontalArrangement.defaultIfNull()) {
            "SpaceBetween" -> Arrangement.SpaceBetween
            "Start" -> Arrangement.Start
            "End" -> Arrangement.End
            "SpaceAround" -> Arrangement.SpaceAround
            "SpaceEvenly" -> Arrangement.SpaceEvenly
            else -> Arrangement.Start
        }
    }
    return Arrangement.Start
}


fun CustomView.getVerticalColArrangement(): Arrangement.Vertical {
    if (verticalAlignment.defaultIfNull() != "default") {
        return when (verticalAlignment.defaultIfNull()) {
            "Top" -> Arrangement.Top
            "Bottom" -> Arrangement.Bottom
            "SpaceBetween" -> Arrangement.SpaceBetween
            "Center" -> Arrangement.Center
            "SpaceAround" -> Arrangement.SpaceAround
            "SpaceEvenly" -> Arrangement.SpaceEvenly
            else -> Arrangement.Top
        }
    }
    return Arrangement.Top
}

fun CustomView.getHorizontalColArrangement(): Alignment.Horizontal {
    if (horizontalArrangement.defaultIfNull() != "default") {
        return when (horizontalArrangement.defaultIfNull()) {
            "Start" -> Alignment.Start
            "CenterHorizontally" -> Alignment.CenterHorizontally
            "End" -> Alignment.End
            else -> Alignment.Start
        }
    }
    return Alignment.Start
}

@Composable
fun CustomModifier.getTextStyle(): TextStyle {
    var textStyle = TextStyle()
    when (style.defaultIfNull()) {
        "body1" -> {
            textStyle = MaterialTheme.typography.body1
        }
        "h5" -> {
            textStyle = MaterialTheme.typography.h5
        }
        "caption" -> {
            textStyle = MaterialTheme.typography.caption
        }
        "body2" -> {
            textStyle = MaterialTheme.typography.body2
        }
        "h4" -> {
            textStyle = MaterialTheme.typography.h4
        }
    }

    when (color.defaultIfNull()) {
        "grey" -> {
            textStyle = textStyle.copy(color = Color.Gray)
        }
        "secondary" -> {
            textStyle = textStyle.copy(color = MaterialTheme.colors.secondary)
        }
        else -> {
            if (color.orEmpty().contains("#")) {
                val color = Color(parseColor(color))
                textStyle = textStyle.copy(color = color)
            }
        }
    }

    return textStyle
}

fun Modifier.fromProps(data: CustomModifier): Modifier {
    var modifier = when (data.paddingDirection) {
        "all" -> padding(data.padding.defaultIfNull().dp)
        "vertical" -> padding(vertical = data.padding.defaultIfNull().dp)
        "horizontal" -> padding(horizontal = data.padding.defaultIfNull().dp)
        "top" -> padding(top = data.padding.defaultIfNull().dp)
        "bottom" -> padding(bottom = data.padding.defaultIfNull().dp)
        "end" -> padding(end = data.padding.defaultIfNull().dp)
        "start" -> padding(start = data.padding.defaultIfNull().dp)
        else -> padding(0.dp)
    }


    modifier = when (data.shape) {
        "CircleShape" -> modifier.clip(CircleShape)
        else -> modifier
    }

    modifier = when (data.size.defaultIfNull()) {
        "fillMaxWidth" -> modifier.fillMaxWidth()
        "fillMaxHeight" -> modifier.fillMaxHeight()
        "fillMaxSize" -> modifier.fillMaxSize()
        else -> {
            return if (data.size.defaultIfNull() != "default") {
                modifier.size(data.size?.toIntOrNull().defaultIfNull().dp)
            } else {
                modifier
            }
        }
    }

    return this.then(modifier)
}

fun Int?.defaultIfNull(): Int {
    return this ?: 0
}

fun String?.defaultIfNull(): String {
    return this ?: "default"
}


@Composable
fun DoctorCard() {
        Card {
        Column(modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.doc_picture),
                contentDescription = null,
                modifier = Modifier
                    .clip(CircleShape)
                    .border(
                        width = 1.dp,
                        color = Color.Gray,
                        shape = CircleShape
                    )
                    .size(60.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.padding(start = 8.dp)) {
                    Text(text = "Dr. Brian Leston", style = MaterialTheme.typography.h5)
                    Text(
                        text = "Internal medicine, Primary care",
                        color = Color.Gray,
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(end = 4.dp)
                ) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "1.9 mi", style = MaterialTheme.typography.body2.copy(
                            color = MaterialTheme.colors.secondary
                        ),
                    )
                }
                Text(
                    text = "110 Sutter St, Fl 6, San Francisco, CA, 94104",
                    style = MaterialTheme.typography.caption.copy(
                        color = Color.Gray
                    )
                )
            }

        }
    }
}


//@Composable
//fun DoctorCard() {
//    Card {
//        Column(modifier = Modifier.padding(8.dp)) {
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//            ) {
//                Image(
//                    painter = painterResource(id = R.drawable.doc_picture),
//                    contentDescription = null,
//                    modifier = Modifier
//                        .clip(CircleShape)
//                        .size(60.dp)
//                )
//                Column(modifier = Modifier.padding(start = 8.dp)) {
//                    Text(text = "Dr. Brian Leston", style = MaterialTheme.typography.h5)
//                    Text(
//                        text = "Internal medicine, Primary care",
//                        color = Color.Gray,
//                        modifier = Modifier.padding(vertical = 8.dp)
//                    )
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically,
//                    ) {
//                       Icon(
//                           Icons.Default.LocationOn,
//                           contentDescription = null,
//                           modifier = Modifier.size(16.dp)
//                       )
//                        Text(
//                            text = "1.9 mi", style = MaterialTheme.typography.caption.copy(
//                                color = MaterialTheme.colors.secondary
//                            ),
//                        )
//                    }
//
//                    Text(
//                        text = "110 Sutter St, Fl 6, San Francisco, CA, 94104",
//                        style = MaterialTheme.typography.caption.copy(
//                            color = Color.Gray
//                        )
//                    )
//                }
//            }
//        }
//    }
//}

@Preview(showBackground = true, name = "light")
@Preview(showBackground = true, name = "dark", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun DefaultPreview() {
    ServerDrivenAppTheme {
        Surface {
            DoctorCard()
        }
    }
}