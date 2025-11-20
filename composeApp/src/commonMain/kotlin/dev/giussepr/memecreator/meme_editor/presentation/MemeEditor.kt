package dev.giussepr.memecreator.meme_editor.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.giussepr.memecreator.core.presentation.MemeTemplate
import dev.giussepr.memecreator.core.theme.MemeCreatorTheme
import dev.giussepr.memecreator.meme_editor.presentation.components.MemeTextBox
import memecreator.composeapp.generated.resources.Res
import memecreator.composeapp.generated.resources.meme_template_01
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MemeEditorRoot(
    template: MemeTemplate,
    viewModel: MemeEditorViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    MemeEditorScreen(
        template = template,
        state = state,
        onViewIntent = viewModel::onViewIntent
    )
}

@Composable
fun MemeEditorScreen(
    template: MemeTemplate,
    state: MemeEditorUiState,
    onViewIntent: (MemeEditorViewIntent) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier.fillMaxWidth(),
            painter = painterResource(template.drawable),
            contentDescription = null,
            contentScale = ContentScale.FillWidth
        )
        state.memeTexts.forEach { memeText ->
            MemeTextBox(
                memeText = memeText,
                textBoxInteractionState = state.textBoxInteractionState,
                maxWidth = 500.dp,
                maxHeight = 500.dp,
                onClick = {
                    onViewIntent(MemeEditorViewIntent.OnSelectMemeText(memeText.id))
                },
                onDoubleClick = {
                    onViewIntent(MemeEditorViewIntent.OnEditMemeText(memeText.id))
                },
                onTextChange = {
                    onViewIntent(MemeEditorViewIntent.OnMemeTextChange(memeText.id, it))
                },
                onDeleteClick = {
                    onViewIntent(MemeEditorViewIntent.OnDeleteMemeTextClick(memeText.id))
                }
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    MemeCreatorTheme {
        MemeEditorScreen(
            template = MemeTemplate(
                id = "meme_template_01",
                drawable = Res.drawable.meme_template_01
            ),
            state = MemeEditorUiState(),
            onViewIntent = {},
        )
    }
}
