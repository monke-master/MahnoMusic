package com.monke.machnomusic3.ui.musicFeature.music.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

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