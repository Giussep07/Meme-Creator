package com.giussepr.memecreator.meme_editor.presentation

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import com.giussepr.memecreator.core.presentation.MemeTemplate

sealed interface MemeEditorViewIntent {
    data object OnGoBackClick: MemeEditorViewIntent
    data object OnConfirmLeaveWithoutSaving: MemeEditorViewIntent
    data object OnDismissLeaveWithoutSaving: MemeEditorViewIntent

    data class OnSaveMemeClick(val memeTemplate: MemeTemplate): MemeEditorViewIntent
    data object OnTapOutsideSelectedText: MemeEditorViewIntent

    data object OnAddTextClick: MemeEditorViewIntent
    data class OnSelectMemeText(val id: String): MemeEditorViewIntent
    data class OnEditMemeText(val id: String): MemeEditorViewIntent
    data class OnMemeTextChange(val id: String, val text: String): MemeEditorViewIntent
    data class OnDeleteMemeTextClick(val id: String): MemeEditorViewIntent

    data class OnMemeTextTransformChange(
        val id: String,
        val offset: Offset,
        val rotation: Float,
        val scale: Float
    ): MemeEditorViewIntent

    data class OnContainerSizeChange(val size: IntSize): MemeEditorViewIntent
}
