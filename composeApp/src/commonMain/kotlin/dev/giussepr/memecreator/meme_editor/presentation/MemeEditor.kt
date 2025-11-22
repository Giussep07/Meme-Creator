package dev.giussepr.memecreator.meme_editor.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.giussepr.memecreator.core.presentation.MemeTemplate
import dev.giussepr.memecreator.core.theme.MemeCreatorTheme
import dev.giussepr.memecreator.meme_editor.presentation.components.BottomBar
import dev.giussepr.memecreator.meme_editor.presentation.components.DraggableContainer
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
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures {
                    onViewIntent(MemeEditorViewIntent.OnTapOutsideSelectedText)
                }
            },
        bottomBar = {
            BottomBar(
                modifier = Modifier
                    .navigationBarsPadding(),
                onAddTextClick = {
                    onViewIntent(MemeEditorViewIntent.OnAddTextClick)
                },
                onSaveClick = {
                    onViewIntent(MemeEditorViewIntent.OnSaveMemeClick(template))
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Box {
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .onSizeChanged {
                            onViewIntent(MemeEditorViewIntent.OnContainerSizeChange(it))
                        },
                    painter = painterResource(template.drawable),
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth
                )
                DraggableContainer(
                    children = state.memeTexts,
                    textBoxInteractionState = state.textBoxInteractionState,
                    onChildTransformChanged = { id, offset, rotation, scale ->
                        onViewIntent(
                            MemeEditorViewIntent.OnMemeTextTransformChange(
                                id,
                                offset,
                                rotation,
                                scale
                            )
                        )
                    },
                    onChildClick = {
                        onViewIntent(MemeEditorViewIntent.OnSelectMemeText(it))
                    },
                    onChildDoubleClick = {
                        onViewIntent(MemeEditorViewIntent.OnEditMemeText(it))
                    },
                    onChildTextChange = { it, text ->
                        onViewIntent(MemeEditorViewIntent.OnMemeTextChange(it, text))
                    },
                    onChildDeleteClick = {
                        onViewIntent(MemeEditorViewIntent.OnDeleteMemeTextClick(it))
                    },
                    modifier = Modifier
                        .matchParentSize()
                )
            }
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
