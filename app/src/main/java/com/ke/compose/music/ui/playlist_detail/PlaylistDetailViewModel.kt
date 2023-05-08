package com.ke.compose.music.ui.playlist_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ke.compose.music.domain.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaylistDetailViewModel @Inject constructor(
    private val getPlaylistDetailUseCase: GetPlaylistDetailUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow<PlaylistDetailUiState>(PlaylistDetailUiState.Loading)

    internal val uiState: StateFlow<PlaylistDetailUiState>
        get() = _uiState

    private val id = savedStateHandle.get<String>("id")!!.toLong()

    init {
        loadData()
    }

    internal fun loadData() {
        viewModelScope.launch {
            _uiState.value = PlaylistDetailUiState.Loading
            when (val result = getPlaylistDetailUseCase(id)) {
                is Result.Error -> {
                    _uiState.value = PlaylistDetailUiState.Error
                }

                is Result.Success -> {
                    _uiState.value = result.data
                }
            }
        }
    }

    internal fun toggleBooked(detail: PlaylistDetailUiState.Detail) {
        _uiState.value = detail.copy(
            subscribed = !detail.subscribed
        )
    }

}