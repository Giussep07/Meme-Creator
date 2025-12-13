package com.giussepr.memecreator.meme_editor.presentation.components

import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import com.giussepr.memecreator.meme_editor.presentation.MemeText
import com.giussepr.memecreator.meme_editor.presentation.TextBoxInteractionState
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun DraggableContainer(
    children: List<MemeText>,
    textBoxInteractionState: TextBoxInteractionState,
    onChildTransformChanged: (id: String, offset: Offset, rotation: Float, scale: Float) -> Unit,
    onChildClick: (String) -> Unit,
    onChildDoubleClick: (String) -> Unit,
    onChildTextChange: (id: String, text: String) -> Unit,
    onChildDeleteClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    BoxWithConstraints(modifier) {
        val parentWidth = constraints.maxWidth
        val parentHeight = constraints.maxHeight

        children.forEach { child ->
            var childWidth by remember(child.id) {
                mutableStateOf(0)
            }
            var childHeight by remember(child.id) {
                mutableStateOf(0)
            }

            val transformableState = rememberTransformableState { scaleChange, panChange, rotationChange ->
                val newRotation = child.rotation + rotationChange

                val angle = newRotation * PI.toFloat() / 180f
                val cos = cos(angle)
                val sin = sin(angle)

                val rotatedPanX = panChange.x * cos - panChange.y * sin
                val rotatedPanY = panChange.x * sin + panChange.y * cos

                val newScale = (child.scale * scaleChange).coerceIn(0.5f, 2f)

                val scaledWidth = childWidth * child.scale
                val scaledHeight = childHeight * child.scale

                val visualWidth = abs(scaledWidth * cos) + abs(scaledHeight * sin)
                val visualHeight = abs(scaledWidth * sin) + abs(scaledHeight * cos)

                val scaleOffsetX = (scaledWidth - childWidth) / 2
                val scaleOffsetY = (scaledHeight - childHeight) / 2

                val rotationOffsetX = (visualWidth - scaledWidth) / 2
                val rotationOffsetY = (visualHeight - scaledHeight) / 2

                val minX = scaleOffsetX + rotationOffsetX
                val maxX = parentWidth - childWidth - scaleOffsetX - rotationOffsetX
                val minY = scaleOffsetY + rotationOffsetY
                val maxY = parentHeight - childHeight - scaleOffsetY - rotationOffsetY

                val newOffset = Offset(
                    x = (child.offsetRatioX * parentWidth + child.scale * rotatedPanX).coerceIn(
                        minimumValue = minOf(minX, maxX),
                        maximumValue = maxOf(minX, maxX)
                    ),
                    y = (child.offsetRatioY * parentHeight + child.scale * rotatedPanY).coerceIn(
                        minimumValue = minOf(minY, maxY),
                        maximumValue = maxOf(minY, maxY)
                    ),
                )

                onChildTransformChanged(
                    child.id,
                    newOffset,
                    newRotation,
                    newScale
                )
            }

            Box(
                modifier = Modifier
                    .onSizeChanged {
                        childWidth = it.width
                        childHeight = it.height
                    }
                    .graphicsLayer {
                        translationX = child.offsetRatioX * parentWidth
                        translationY = child.offsetRatioY * parentHeight
                        rotationZ = child.rotation
                        scaleX = child.scale
                        scaleY = child.scale
                    }
                    .transformable(transformableState)
            ) {
                MemeTextBox(
                    memeText = child,
                    textBoxInteractionState = textBoxInteractionState,
                    maxWidth = with(density) {
                        parentWidth.toDp()
                    },
                    maxHeight = with(density) {
                        parentHeight.toDp()
                    },
                    onClick = {
                        onChildClick(child.id)
                    },
                    onDoubleClick = {
                        onChildDoubleClick(child.id)
                    },
                    onTextChange = {
                        onChildTextChange(child.id, it)
                    },
                    onDeleteClick = {
                        onChildDeleteClick(child.id)
                    }
                )
            }
        }
    }
}