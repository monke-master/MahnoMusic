package com.monke.machnomusic3.main.activity

import com.monke.machnomusic3.domain.usecase.music.GetCurrentTrackUseCase
import com.monke.machnomusic3.domain.usecase.music.GetMusicStateUseCase
import com.monke.machnomusic3.domain.usecase.music.NextTrackUseCase
import javax.inject.Inject

class ActivityUseCases @Inject constructor(
    val getCurrentTrackUseCase: GetCurrentTrackUseCase,
    val getMusicStateUseCase: GetMusicStateUseCase,
    val nextTrackUseCase: NextTrackUseCase
)