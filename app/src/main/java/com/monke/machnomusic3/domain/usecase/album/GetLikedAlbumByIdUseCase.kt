package com.monke.machnomusic3.domain.usecase.album

import com.monke.machnomusic3.domain.model.Album
import com.monke.machnomusic3.domain.repository.AlbumRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetLikedAlbumByIdUseCase @Inject constructor(
    private val albumRepository: AlbumRepository
) {

    suspend fun execute(albumId: String): Album? {
        val albumsList = albumRepository.userAlbumsList.first()
        return albumsList.find { it.id ==  albumId }
    }
}