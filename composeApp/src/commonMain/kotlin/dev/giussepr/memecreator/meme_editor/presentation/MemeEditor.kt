@file:OptIn(ExperimentalComposeUiApi::class)

package dev.giussepr.memecreator.meme_editor.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.adaptive.currentWindowSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.giussepr.memecreator.core.presentation.MemeTemplate
import dev.giussepr.memecreator.core.theme.MemeCreatorTheme
import dev.giussepr.memecreator.meme_editor.presentation.components.BottomBar
import dev.giussepr.memecreator.meme_editor.presentation.components.ConfirmationDialog
import dev.giussepr.memecreator.meme_editor.presentation.components.ConfirmationDialogConfig
import dev.giussepr.memecreator.meme_editor.presentation.components.DraggableContainer
import memecreator.composeapp.generated.resources.Res
import memecreator.composeapp.generated.resources.cancel
import memecreator.composeapp.generated.resources.leave
import memecreator.composeapp.generated.resources.leave_editor_message
import memecreator.composeapp.generated.resources.leave_editor_title
import memecreator.composeapp.generated.resources.meme_template_01
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MemeEditorRoot(
    template: MemeTemplate,
    onBackClick: () -> Unit,
    viewModel: MemeEditorViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.hasLeftEditor) {
        if (state.hasLeftEditor) {
            onBackClick()
        }
    }

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
    BackHandler(
        enabled = state.isLeavingWithoutSaving.not()
    ) {
        onViewIntent(MemeEditorViewIntent.OnGoBackClick)
    }

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
            val windowSize = currentWindowSize()
            Box {
                Image(
                    modifier = Modifier
                        .then(
                            if (windowSize.width > windowSize.height) {
                                Modifier.fillMaxHeight()
                            } else Modifier.fillMaxWidth()
                        )
                        .onSizeChanged {
                            onViewIntent(MemeEditorViewIntent.OnContainerSizeChange(it))
                        },
                    painter = painterResource(template.drawable),
                    contentDescription = null,
                    contentScale = if (windowSize.width > windowSize.height) {
                        ContentScale.FillHeight
                    } else ContentScale.FillWidth
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

            IconButton(
                onClick = {
                    onViewIntent(MemeEditorViewIntent.OnGoBackClick)
                },
                modifier = Modifier
                    .align(Alignment.TopStart)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Navigate back"
                )
            }
        }
    }

    if (state.isLeavingWithoutSaving) {
        ConfirmationDialog(
            config = ConfirmationDialogConfig(
                title = stringResource(Res.string.leave_editor_title),
                message = stringResource(Res.string.leave_editor_message),
                confirmButtonText = stringResource(Res.string.leave),
                cancelButtonText = stringResource(Res.string.cancel),
                confirmButtonColor = MaterialTheme.colorScheme.secondary
            ),
            onConfirm = {
                onViewIntent(MemeEditorViewIntent.OnConfirmLeaveWithoutSaving)
            },
            onDismiss = {
                onViewIntent(MemeEditorViewIntent.OnDismissLeaveWithoutSaving)
            }
        )
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
