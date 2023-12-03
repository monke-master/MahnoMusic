package com.monke.machnomusic3.domain.usecase.playlist

import android.net.Uri
import com.monke.machnomusic3.domain.exception.NotFoundException
import com.monke.machnomusic3.domain.model.Playlist
import com.monke.machnomusic3.domain.repository.PlaylistRepository
import com.monke.machnomusic3.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.UUID
import javax.inject.Inject

class UploadPlaylistUseCase @Inject constructor(
    private val playlistRepository: PlaylistRepository,
    private val userRepository: UserRepository
) {

    suspend fun execute(
        tracksIdsList: List<String>,
        title: String,
        coverUri: Uri,
        description: String?
    ) = withContext(Dispatchers.IO) {
        // Получение автора
        val author = userRepository.user.first()
            ?: return@withContext Result.failure(NotFoundException())

        // Инициализация плейлиста
        val playlistId = UUID.randomUUID().toString()
        val playlist = Playlist(
            id = playlistId,
            author = author,
            title = title,
            description = description,
            tracksIdsList = tracksIdsList,
            coverId = playlistId,
            creationDate = Calendar.getInstance().timeInMillis
        )

        // Загрузка плейлиста
        val result = playlistRepository.uploadPlaylist(playlist, coverUri)
        if (result.isFailure)
            return@withContext result

        // Обновление данных пользователя
        return@withContext userRepository.updateUser(
            author.copy(
                playlistsIdsList = author.playlistsIdsList + playlistId
            )
        )
    }
}