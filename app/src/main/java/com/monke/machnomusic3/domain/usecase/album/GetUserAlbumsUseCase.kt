package com.monke.machnomusic3.domain.usecase.album

import com.monke.machnomusic3.domain.repository.AlbumRepository
import javax.inject.Inject

class GetUserAlbumsUseCase @Inject constructor(
    private val albumRepository: AlbumRepository
) {

    fun execute() = albumRepository.userAlbumsList

}