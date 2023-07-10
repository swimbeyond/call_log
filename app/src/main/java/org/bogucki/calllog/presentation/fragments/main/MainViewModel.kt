package org.bogucki.calllog.presentation.fragments.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.bogucki.calllog.domain.models.CallLogEntry
import org.bogucki.calllog.domain.usecases.GetCallLogUseCase
import org.bogucki.calllog.domain.usecases.GetServerAddressUseCase

class MainViewModel(
    private val getCallLog: GetCallLogUseCase,
    private val getServerAddressUseCase: GetServerAddressUseCase
): ViewModel() {

    private val _state = MutableStateFlow(State())
    val state: StateFlow<State> = _state

    fun observeCallLog() {
        getCallLog().onEach {
            _state.emit(_state.value.copy(callLog = it))
        }.launchIn(viewModelScope)
    }

    fun getIpAddress() {
        val address = getServerAddressUseCase()
        _state.value = _state.value.copy(ipAddress = address)
    }

    data class State(
        val ipAddress: String? = null,
        val callLog: List<CallLogEntry> = listOf()
    )
}