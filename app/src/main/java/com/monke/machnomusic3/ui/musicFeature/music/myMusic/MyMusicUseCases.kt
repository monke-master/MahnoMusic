package com.monke.machnomusic3.ui.musicFeature.music.myMusic

import com.monke.machnomusic3.domain.usecase.album.GetUserAlbumsUseCase
import com.monke.machnomusic3.domain.usecase.album.LoadAlbumsListUseCase
import com.monke.machnomusic3.domain.usecase.musicPlayer.PlayTrackListUseCase
import com.monke.machnomusic3.domain.usecase.track.GetTrackCoverUrlUseCase
import com.monke.machnomusic3.domain.usecase.track.GetUserTracksUseCase
import com.monke.machnomusic3.domain.usecase.track.LoadTrackUseCase
import javax.inject.Inject

class MyMusicUseCases @Inject constructor(
    val playTrackListUseCase: PlayTrackListUseCase,
    val loadTrackUseCase: LoadTrackUseCase,
    val getUserTracksUseCase: GetUserTracksUseCase,
    val getTrackCoverUrlUseCase: GetTrackCoverUrlUseCase,
    val getUserAlbumsUseCase: GetUserAlbumsUseCase,
    val loadAlbumsListUseCase: LoadAlbumsListUseCase
)