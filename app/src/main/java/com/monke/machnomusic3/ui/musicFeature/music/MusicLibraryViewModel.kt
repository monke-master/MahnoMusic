package com.monke.machnomusic3.ui.musicFeature.music

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.monke.machnomusic3.domain.model.Track
import com.monke.machnomusic3.domain.usecase.music.PlayTrackListUseCase
import com.monke.machnomusic3.ui.musicFeature.musicPlayer.MiniPlayerViewModel

class MusicLibraryViewModel(

): ViewModel() {




    class Factory(

    ): ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MusicLibraryViewModel(

            ) as T
        }
    }

}