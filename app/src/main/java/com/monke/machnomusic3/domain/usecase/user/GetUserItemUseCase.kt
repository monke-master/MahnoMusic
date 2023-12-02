package com.monke.machnomusic3.domain.usecase.user

import com.monke.machnomusic3.domain.exception.NotFoundException
import com.monke.machnomusic3.domain.model.User
import com.monke.machnomusic3.ui.uiModels.UserItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetUserItemUseCase @Inject constructor(
    private val getProfilePicUrlUseCase: GetProfilePicUrlUseCase
) {

    suspend fun execute(
        user: User
    ): Result<UserItem> = withContext(Dispatchers.IO) {
        val profilePicUrl = user.profilePicId?.let {
            getProfilePicUrlUseCase.execute(it).getOrNull()
                ?: return@withContext Result.failure(NotFoundException())
        }
        val item = UserItem(
            user = user,
            profilePicUrl = profilePicUrl
        )
        return@withContext Result.success(item)
    }
}