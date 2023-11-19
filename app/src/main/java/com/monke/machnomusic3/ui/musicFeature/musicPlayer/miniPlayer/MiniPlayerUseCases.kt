package com.monke.machnomusic3.ui.musicFeature.musicPlayer.miniPlayer

import com.monke.machnomusic3.domain.usecase.music.GetCurrentTrackUseCase
import com.monke.machnomusic3.domain.usecase.music.GetMusicStateUseCase
import com.monke.machnomusic3.domain.usecase.music.UpdateMusicStateUseCase
import javax.inject.Inject


class MiniPlayerUseCases @Inject constructor(
    val getCurrentTrackUseCase: GetCurrentTrackUseCase,
    val getMusicStateUseCase: GetMusicStateUseCase,
    val updateMusicStateUseCase: UpdateMusicStateUseCase
 )