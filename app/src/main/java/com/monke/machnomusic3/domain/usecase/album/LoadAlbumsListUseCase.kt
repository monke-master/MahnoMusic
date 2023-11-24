package com.monke.machnomusic3.domain.usecase.album

import com.monke.machnomusic3.domain.exception.NotFoundException
import com.monke.machnomusic3.domain.repository.AlbumRepository
import com.monke.machnomusic3.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoadAlbumsListUseCase @Inject constructor(
    private val albumRepository: AlbumRepository,
    private val userRepository: UserRepository
) {

    suspend fun execute() = withContext(Dispatchers.IO) {
        val user = userRepository.user.first()
            ?: return@withContext Result.failure(NotFoundException())
        albumRepository.loadAlbums(user)
    }
}