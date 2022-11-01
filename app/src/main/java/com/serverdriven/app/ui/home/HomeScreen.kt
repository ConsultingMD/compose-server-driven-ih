package com.serverdriven.app.ui.home

import com.serverdriven.app.R
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.serverdriven.app.data.models.CustomView
import com.serverdriven.app.data.models.TypeView
import com.serverdriven.app.utils.*


@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel()
) {
    val context = LocalContext.current
    val data by viewModel.dataState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getData(context)
    }

    AnimatedVisibility(visible = data.error?.isNotEmpty() == true) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = stringResource(R.string.error, data.error.orEmpty()), color = Color.Red)
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
            Text(text = stringResource(id = R.string.next_design))
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(R.string.current_design, viewModel.fileName.collectAsState().value),
            modifier = Modifier.padding(top = 16.dp))
    }
}


@Composable
fun SetUpView(children: List<CustomView>) {
    children.forEach { child ->
        when (child.typeName) {
            TypeView.UNKNOWN -> {
                Spacer(modifier = Modifier.requiredSize(0.dp))
            }
            TypeView.CARD -> {
                Card(modifier = Modifier.fromProps(child.modifier)) {
                    SetUpView(children = child.children)
                }
            }
            TypeView.COLUMN -> {
                Column(
                    modifier = Modifier.fromProps(child.modifier),
                    verticalArrangement = child.getVerticalColArrangement(),
                    horizontalAlignment = child.getHorizontalColArrangement()
                ) {
                    SetUpView(children = child.children)
                }
            }
            TypeView.ROW -> {
                Row(
                    modifier = Modifier.fromProps(child.modifier),
                    horizontalArrangement = child.getHorizontalRowArrangement(),
                    verticalAlignment = child.getVerticalRowArrangement()
                ) {
                    SetUpView(children = child.children)
                }
            }
            TypeView.TEXT -> {
                Text(
                    text = child.value.orEmpty(),
                    style = child.modifier.getTextStyle(),
                    modifier = Modifier.fromProps(child.modifier)
                )
            }
            TypeView.IMAGE -> {
                val source =
                    rememberAsyncImagePainter(child.value, contentScale = ContentScale.Crop)
                Image(
                    painter = source,
                    contentDescription = null,
                    modifier = Modifier.fromProps(child.modifier)
                )
            }
            TypeView.ICON -> {
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