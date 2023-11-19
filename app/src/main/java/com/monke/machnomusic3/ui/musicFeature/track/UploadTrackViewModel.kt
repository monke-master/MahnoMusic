package com.monke.machnomusic3.ui.musicFeature.track

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.monke.machnomusic3.domain.usecase.music.PlayTrackListUseCase
import com.monke.machnomusic3.ui.musicFeature.music.MyMusicViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class UploadTrackViewModel : ViewModel() {


    private var trackUri: Uri? = null

    private val _trackTitle = MutableStateFlow("")
    val trackTitle = _trackTitle.asStateFlow()

    fun setTrackUri(uri: Uri?) {
        trackUri = uri
    }

    fun setTrackTitle(title: String) {
        _trackTitle.value = title
    }

    class Factory @Inject constructor(
        private val playTrackListUseCase: PlayTrackListUseCase
    ): ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return UploadTrackViewModel(

            ) as T
        }
    }


}