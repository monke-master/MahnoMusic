package com.monke.machnomusic3.ui.musicFeature.musicPlayer

import com.monke.machnomusic3.domain.usecase.musicPlayer.GetCurrentTrackUseCase
import com.monke.machnomusic3.domain.usecase.musicPlayer.GetMusicStateUseCase
import com.monke.machnomusic3.domain.usecase.musicPlayer.GetTrackProgressUseCase
import com.monke.machnomusic3.domain.usecase.musicPlayer.NextTrackUseCase
import com.monke.machnomusic3.domain.usecase.musicPlayer.PrevTrackUseCase
import com.monke.machnomusic3.domain.usecase.musicPlayer.SetTrackProgressUseCase
import com.monke.machnomusic3.domain.usecase.musicPlayer.UpdateMusicStateUseCase
import com.monke.machnomusic3.domain.usecase.track.GetTrackCoverUrlUseCase
import javax.inject.Inject


class MusicPlayerUseCases @Inject constructor(
    val getCurrentTrackUseCase: GetCurrentTrackUseCase,
    val getMusicStateUseCase: GetMusicStateUseCase,
    val updateMusicStateUseCase: UpdateMusicStateUseCase,
    val nextTrackUseCase: NextTrackUseCase,
    val prevTrackUseCase: PrevTrackUseCase,
    val getTrackProgressUseCase: GetTrackProgressUseCase,
    val setTrackProgressUseCase: SetTrackProgressUseCase,
    val getTrackCoverUrlUseCase: GetTrackCoverUrlUseCase
 )