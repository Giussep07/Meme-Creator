package dev.giussepr.memecreator.meme_editor.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.giussepr.memecreator.meme_editor.presentation.MemeText
import dev.giussepr.memecreator.meme_editor.presentation.TextBoxInteractionState
import dev.giussepr.memecreator.meme_editor.presentation.utils.rememberFillTextStyle
import dev.giussepr.memecreator.meme_editor.presentation.utils.rememberStrokeTextStyle
import kotlinx.coroutines.delay

@Composable
fun MemeTextBox(
    modifier: Modifier = Modifier,
    memeText: MemeText,
    textBoxInteractionState: TextBoxInteractionState,
    maxWidth: Dp,
    maxHeight: Dp,
    onClick: () -> Unit,
    onDoubleClick: () -> Unit,
    onTextChange: (String) -> Unit,
    onDeleteClick: () -> Unit,
) {
    val memeFocusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(textBoxInteractionState) {
        if (textBoxInteractionState is TextBoxInteractionState.Editing) {
            memeFocusRequester.requestFocus()
            delay(100)
            keyboardController?.show()
        }
    }

    LaunchedEffect(textBoxInteractionState, memeText.id) {
        if (textBoxInteractionState !is TextBoxInteractionState.Selected) {
            focusManager.clearFocus()
        }
    }

    Box(modifier = modifier) {
        val isMemeTextSelected =
            (textBoxInteractionState is TextBoxInteractionState.Selected && textBoxInteractionState.textBoxId == memeText.id) ||
                    (textBoxInteractionState is TextBoxInteractionState.Editing && textBoxInteractionState.textBoxId == memeText.id)

        Box(
            modifier = Modifier
                .sizeIn(maxWidth = maxWidth, maxHeight = maxHeight)
                .border(
                    width = 2.dp,
                    color = if (isMemeTextSelected) {
                        Color.White
                    } else Color.Transparent,
                    shape = RoundedCornerShape(4.dp)
                )
                .background(
                    color = if (textBoxInteractionState is TextBoxInteractionState.Editing && textBoxInteractionState.textBoxId == memeText.id) {
                        Color.Black.copy(alpha = 0.15f)
                    } else Color.Transparent,
                    shape = RoundedCornerShape(4.dp)
                )
                .combinedClickable(
                    onClick = onClick,
                    onDoubleClick = onDoubleClick
                )
        ) {
            val strokeTextStyle = rememberStrokeTextStyle()
            val fillTextStyle = rememberFillTextStyle()
            val textPadding = 8.dp
            if (textBoxInteractionState is TextBoxInteractionState.Editing && textBoxInteractionState.textBoxId == memeText.id) {
                OutlinedImpactTextField(
                    text = memeText.text,
                    onTextChange = onTextChange,
                    strokeTextStyle = strokeTextStyle,
                    fillTextStyle = fillTextStyle,
                    maxWidth = maxWidth - (textPadding * 2),
                    maxHeight = maxHeight - (textPadding * 2),
                    modifier = Modifier
                        .focusRequester(memeFocusRequester)
                        .padding(textPadding)
                )
            } else {
                OutlinedImpactText(
                    text = memeText.text,
                    strokeTextStyle = strokeTextStyle,
                    fillTextStyle = fillTextStyle,
                    modifier = Modifier
                        .padding(textPadding)
                )
            }
        }

        if (isMemeTextSelected) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.TopEnd)
                    .offset(
                        x = 12.dp,
                        y = -(12).dp
                    )
                    .clip(CircleShape)
                    .background(Color(0xFFB3261E))
                    .clickable {
                        onDeleteClick()
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
