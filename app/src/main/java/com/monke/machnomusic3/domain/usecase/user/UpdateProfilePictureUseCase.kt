package com.monke.machnomusic3.domain.usecase.user

import android.net.Uri
import com.monke.machnomusic3.domain.exception.NotFoundException
import com.monke.machnomusic3.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

class UpdateProfilePictureUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    suspend fun execute(
        uri: Uri
    ): Result<Any?> = withContext(Dispatchers.IO) {
        val pictureId = UUID.randomUUID().toString()
        val result = userRepository.updateProfilePicture(uri, pictureId)
        if (result.isFailure) return@withContext result
        val user = userRepository.user.first()?.copy(profilePicId = pictureId)
            ?: return@withContext Result.failure(NotFoundException())
        return@withContext userRepository.updateUser(user)
    }

}