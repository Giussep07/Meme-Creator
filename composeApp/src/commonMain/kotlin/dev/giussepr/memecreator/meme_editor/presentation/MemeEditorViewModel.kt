package dev.giussepr.memecreator.meme_editor.presentation

import androidx.compose.ui.unit.IntSize
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class MemeEditorViewModel : ViewModel() {
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
            MemeEditorViewIntent.OnAddTextClick -> TODO()
            MemeEditorViewIntent.OnConfirmLeaveWithoutSaving -> TODO()
            is MemeEditorViewIntent.OnContainerSizeChange -> updateContainerSize(event.size)
            is MemeEditorViewIntent.OnDeleteMemeTextClick -> TODO()
            MemeEditorViewIntent.OnDismissLeaveWithoutSaving -> TODO()
            is MemeEditorViewIntent.OnEditMemeText -> TODO()
            MemeEditorViewIntent.OnGoBackClick -> TODO()
            is MemeEditorViewIntent.OnMemeTextChange -> TODO()
            is MemeEditorViewIntent.OnMemeTextTransformChange -> TODO()
            is MemeEditorViewIntent.OnSaveMemeClick -> TODO()
            is MemeEditorViewIntent.OnSelectMemeText -> TODO()
            MemeEditorViewIntent.OnTapOutsideSelectedText -> TODO()
        }
    }

    private fun updateContainerSize(size: IntSize) {
        _state.update { it.copy(
            templateSize = size
        ) }
    }
}