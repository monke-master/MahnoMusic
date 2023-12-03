package com.monke.machnomusic3.ui.mainFeature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.monke.machnomusic3.domain.usecase.musicPlayer.GetMusicStateUseCase
import com.monke.machnomusic3.ui.musicFeature.music.myMusic.MyMusicViewModel
import javax.inject.Inject

class MainViewModel(
    useCases: UseCases
) : ViewModel() {

    data class UseCases @Inject constructor(
        val getMusicStateUseCase: GetMusicStateUseCase
    )

    private val getMusicStateUseCase = useCases.getMusicStateUseCase

    val musicState = getMusicStateUseCase.execute()

    class Factory @Inject constructor(
        private val useCases: UseCases
    ): ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel(useCases) as T
        }
    }

}