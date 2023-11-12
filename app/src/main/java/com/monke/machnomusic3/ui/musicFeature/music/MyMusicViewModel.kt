package com.monke.machnomusic3.ui.musicFeature.music

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.monke.machnomusic3.domain.model.Track
import com.monke.machnomusic3.domain.usecase.music.PlayTrackListUseCase
import javax.inject.Inject

class MyMusicViewModel(
    private val playTrackListUseCase: PlayTrackListUseCase
): ViewModel() {

    fun playTrackList(
        trackList: List<Track>,
        index: Int
    ) {
        playTrackListUseCase.execute(trackList, index)
    }

    class Factory @Inject constructor(
        private val playTrackListUseCase: PlayTrackListUseCase
    ): ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MyMusicViewModel(
                playTrackListUseCase = playTrackListUseCase
            ) as T
        }
    }

}