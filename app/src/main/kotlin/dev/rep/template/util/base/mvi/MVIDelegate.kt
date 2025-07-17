package dev.rep.template.util.base.mvi

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.getValue

class MVIDelegate<S : UiState, A: UiAction, E: SideEffect> internal constructor(
    initialUiState: S,
) : MVI<S, A, E>  {

    private val _uiState = MutableStateFlow(initialUiState)
    override val uiState: StateFlow<S> = _uiState.asStateFlow()

    private val _sideEffect by lazy { Channel<E>() }
    override val sideEffect: Flow<E> by lazy { _sideEffect.receiveAsFlow() }

    override fun onAction(uiAction: A) {}

    override fun updateUiState(block: S.() -> S) {
        _uiState.update(block)
    }

    override fun updateUiState(newUiState: S) {
        _uiState.update { newUiState }
    }

    override fun CoroutineScope.emitSideEffect(effect: E) {
        this.launch { _sideEffect.send(effect) }
    }
}

fun <S : UiState, A: UiAction, E: SideEffect> mvi(
    initialUiState: S,
): MVI<S, A, E> = MVIDelegate( initialUiState)