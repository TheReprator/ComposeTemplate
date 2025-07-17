package dev.rep.template.util.base.mvi

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface UiState

interface UiAction

interface SideEffect

interface MVI<S : UiState, A: UiAction, E: SideEffect> {
    val uiState: StateFlow<S>

    val sideEffect: Flow<E>

    fun onAction(uiAction: A)

    fun updateUiState(block: S.() -> S)

    fun updateUiState(newUiState: S)

    fun CoroutineScope.emitSideEffect(effect: E)
}
