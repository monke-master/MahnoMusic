package com.monke.triviamasters.domain.useCases.user

import com.monke.machnomusic3.domain.model.User
import com.monke.machnomusic3.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    suspend fun execute(email: String, password: String): Result<Any?> =
        withContext(Dispatchers.IO) {
            val reposResult = userRepository.signIn(email, password)
            if (reposResult.isSuccess) {
                val user = reposResult.getOrNull() as User
                userRepository.updateUser(user)
                return@withContext reposResult
            } else {
                return@withContext reposResult
            }
        }
}