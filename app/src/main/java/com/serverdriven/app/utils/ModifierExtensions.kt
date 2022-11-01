package com.serverdriven.app.utils

import android.graphics.Color.parseColor
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.serverdriven.app.data.models.CustomModifier
import com.serverdriven.app.data.models.CustomView

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