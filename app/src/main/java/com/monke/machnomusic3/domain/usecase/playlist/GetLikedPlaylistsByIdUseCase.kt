package com.monke.machnomusic3.domain.usecase.playlist

import com.monke.machnomusic3.domain.model.Playlist
import com.monke.machnomusic3.domain.repository.PlaylistRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetLikedPlaylistsByIdUseCase @Inject constructor(
    private val playlistRepository: PlaylistRepository
) {

    suspend fun execute(
        playlistId: String
    ): Playlist? = withContext(Dispatchers.IO) {
        val playlistsList = playlistRepository.userPlaylistsList.first()
        playlistsList.find { it.id == playlistId }
    }
}