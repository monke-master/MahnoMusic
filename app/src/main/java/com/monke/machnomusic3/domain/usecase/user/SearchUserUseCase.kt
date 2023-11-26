package com.monke.machnomusic3.domain.usecase.user

import com.monke.machnomusic3.domain.model.Track
import com.monke.machnomusic3.domain.model.User
import com.monke.machnomusic3.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchUserUseCase @Inject constructor(
    private val userRep: UserRepository
) {

    suspend fun execute(
        query: String
    ): Result<List<User>> = withContext(Dispatchers.IO) {
        userRep.searchUser(query)
    }

}