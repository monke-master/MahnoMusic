package com.monke.machnomusic3.main.activity

import com.monke.machnomusic3.domain.usecase.musicPlayer.GetCurrentTrackUseCase
import com.monke.machnomusic3.domain.usecase.musicPlayer.GetMusicStateUseCase
import com.monke.machnomusic3.domain.usecase.musicPlayer.GetTrackProgressUseCase
import com.monke.machnomusic3.domain.usecase.musicPlayer.NextTrackUseCase
import com.monke.machnomusic3.domain.usecase.musicPlayer.SetTrackProgressUseCase
import javax.inject.Inject

class ActivityUseCases @Inject constructor(
    val getCurrentTrackUseCase: GetCurrentTrackUseCase,
    val getMusicStateUseCase: GetMusicStateUseCase,
    val nextTrackUseCase: NextTrackUseCase,
    val setTrackProgressUseCase: SetTrackProgressUseCase,
    val getTrackProgressUseCase: GetTrackProgressUseCase
)