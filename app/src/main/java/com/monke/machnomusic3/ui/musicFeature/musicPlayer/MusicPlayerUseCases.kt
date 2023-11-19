package com.monke.machnomusic3.ui.musicFeature.musicPlayer

import com.monke.machnomusic3.domain.usecase.music.GetCurrentTrackUseCase
import com.monke.machnomusic3.domain.usecase.music.GetMusicStateUseCase
import com.monke.machnomusic3.domain.usecase.music.NextTrackUseCase
import com.monke.machnomusic3.domain.usecase.music.PrevTrackUseCase
import com.monke.machnomusic3.domain.usecase.music.UpdateMusicStateUseCase
import javax.inject.Inject


class MusicPlayerUseCases @Inject constructor(
    val getCurrentTrackUseCase: GetCurrentTrackUseCase,
    val getMusicStateUseCase: GetMusicStateUseCase,
    val updateMusicStateUseCase: UpdateMusicStateUseCase,
    val nextTrackUseCase: NextTrackUseCase,
    val prevTrackUseCase: PrevTrackUseCase
 )