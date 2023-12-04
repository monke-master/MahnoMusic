package com.monke.machnomusic3.domain.usecase.user

import com.monke.machnomusic3.domain.model.User
import com.monke.machnomusic3.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UnfollowUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    suspend fun execute(
        user: User,
        follower: User
    ) = withContext(Dispatchers.IO) {
        val result = userRepository.updateUser(
            user.copy(subscribersIdsList = user.subscribersIdsList - follower.id)
        )
        if (result.isFailure) return@withContext result
        return@withContext userRepository.updateUser(
            follower.copy(subscriptionsIdsList = follower.subscriptionsIdsList - user.id)
        )
    }
}