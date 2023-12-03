package com.monke.machnomusic3.domain.usecase.playlist

import com.monke.machnomusic3.domain.repository.AlbumRepository
import com.monke.machnomusic3.domain.repository.PlaylistRepository
import javax.inject.Inject

class GetUserPlaylistsUseCase @Inject constructor(
    private val playlistRepository: PlaylistRepository
) {

    fun execute() = playlistRepository.userPlaylistsList

}