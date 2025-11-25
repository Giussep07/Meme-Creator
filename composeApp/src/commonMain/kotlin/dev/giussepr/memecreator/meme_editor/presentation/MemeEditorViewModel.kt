@file:OptIn(ExperimentalUuidApi::class)

package dev.giussepr.memecreator.meme_editor.presentation

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.giussepr.memecreator.core.presentation.MemeTemplate
import dev.giussepr.memecreator.meme_editor.domain.MemeExporter
import dev.giussepr.memecreator.meme_editor.domain.SaveToStorageStrategy
import dev.giussepr.memecreator.meme_editor.presentation.utils.PlatformShareSheet
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getDrawableResourceBytes
import org.jetbrains.compose.resources.getSystemResourceEnvironment
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class MemeEditorViewModel(
    private val memeExporter: MemeExporter,
    private val storageStrategy: SaveToStorageStrategy,
    private val platformShareSheet: PlatformShareSheet
) : ViewModel() {
    private var hasLoadedInitialData: Boolean = false

    private val _state = MutableStateFlow(MemeEditorUiState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here */
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = MemeEditorUiState()
        )

    fun onViewIntent(event: MemeEditorViewIntent) {
        when (event) {
            MemeEditorViewIntent.OnAddTextClick -> addText()
            MemeEditorViewIntent.OnConfirmLeaveWithoutSaving -> confirmLeave()
            is MemeEditorViewIntent.OnContainerSizeChange -> updateContainerSize(event.size)
            is MemeEditorViewIntent.OnDeleteMemeTextClick -> deleteMemeText(event.id)
            MemeEditorViewIntent.OnDismissLeaveWithoutSaving -> dismissConfirmLeaveDialog()
            is MemeEditorViewIntent.OnEditMemeText -> editMemeText(event.id)
            MemeEditorViewIntent.OnGoBackClick -> attemptToGoBack()
            is MemeEditorViewIntent.OnMemeTextChange -> updateMemeText(event.id, event.text)
            is MemeEditorViewIntent.OnMemeTextTransformChange -> transformMemeText(
                id = event.id,
                offset = event.offset,
                rotation = event.rotation,
                scale = event.scale
            )

            is MemeEditorViewIntent.OnSaveMemeClick -> saveMeme(event.memeTemplate)
            is MemeEditorViewIntent.OnSelectMemeText -> selectMemeText(event.id)
            MemeEditorViewIntent.OnTapOutsideSelectedText -> unselectMemeText()
        }
    }

    private fun saveMeme(memeTemplate: MemeTemplate) {
        viewModelScope.launch {
            memeExporter
                .exportMeme(
                    backgroundImageBytes = getDrawableResourceBytes(
                        environment = getSystemResourceEnvironment(),
                        resource = memeTemplate.drawable
                    ),
                    memeTexts = state.value.memeTexts,
                    templateSize = state.value.templateSize,
                    saveToStorageStrategy = storageStrategy
                )
                .onSuccess {
                    platformShareSheet.shareFile(it)
                }
                .onFailure {
                    println("Something went wrong, $it")
                }
        }
    }

    private fun dismissConfirmLeaveDialog() {
        _state.update {
            it.copy(
                isLeavingWithoutSaving = false
            )
        }
    }

    private fun confirmLeave() {
        _state.update {
            it.copy(
                hasLeftEditor = true
            )
        }
    }

    private fun attemptToGoBack() {
        if (state.value.memeTexts.isEmpty()) {
            _state.update {
                it.copy(
                    hasLeftEditor = true
                )
            }
        } else {
            _state.update {
                it.copy(
                    isLeavingWithoutSaving = true
                )
            }
        }
    }

    private fun transformMemeText(
        id: String,
        offset: Offset,
        rotation: Float,
        scale: Float
    ) {
        _state.update {
            val (width, height) = it.templateSize
            it.copy(
                memeTexts = it.memeTexts.map { memeText ->
                    if (memeText.id == id) {
                        memeText.copy(
                            offsetRatioX = offset.x / width,
                            offsetRatioY = offset.y / height,
                            rotation = rotation,
                            scale = scale
                        )
                    } else memeText
                }
            )
        }
    }

    private fun unselectMemeText() {
        _state.update {
            it.copy(
                textBoxInteractionState = TextBoxInteractionState.None
            )
        }
    }

    private fun addText() {
        val id = Uuid.random().toString()

        val memeText = MemeText(
            id = id,
            text = "TAP TO EDIT",
            offsetRatioX = 0.25f,
            offsetRatioY = 0.25f,
        )

        _state.update {
            it.copy(
                memeTexts = it.memeTexts + memeText,
                textBoxInteractionState = TextBoxInteractionState.Selected(id)
            )
        }
    }

    private fun deleteMemeText(id: String) {
        _state.update {
            it.copy(
                memeTexts = it.memeTexts.filter { memeText ->
                    memeText.id != id
                }
            )
        }
    }

    private fun selectMemeText(id: String) {
        _state.update {
            it.copy(
                textBoxInteractionState = TextBoxInteractionState.Selected(id)
            )
        }
    }

    private fun updateMemeText(id: String, text: String) {
        _state.update {
            it.copy(
                memeTexts = it.memeTexts.map { memeText ->
                    if (memeText.id == id) {
                        memeText.copy(text = text)
                    } else memeText
                }
            )
        }
    }

    private fun editMemeText(id: String) {
        _state.update {
            it.copy(
                textBoxInteractionState = TextBoxInteractionState.Editing(id)
            )
        }
    }

    private fun updateContainerSize(size: IntSize) {
        _state.update {
            it.copy(
                templateSize = size
            )
        }
    }
}