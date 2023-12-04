package com.monke.machnomusic3.domain.usecase.user

import com.monke.machnomusic3.domain.exception.NotFoundException
import com.monke.machnomusic3.ui.uiModels.UiState
import com.monke.machnomusic3.ui.uiModels.UserItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetUserItemsListUseCase @Inject constructor(
    private val getUserItemUseCase: GetUserItemUseCase,
    private val getUserByIdUseCase: GetUserByIdUseCase
) {

    suspend fun execute(
        idsList: List<String>
    ): Result<List<UserItem>> = withContext(Dispatchers.IO) {
        val items = ArrayList<UserItem>()
        for (id in idsList) {
            val userRes = getUserByIdUseCase.execute(id)
            val user = userRes.getOrNull()
                ?: return@withContext Result.failure(NotFoundException())

            val itemRes = getUserItemUseCase.execute(user)
            val userItem = itemRes.getOrNull()
                ?: return@withContext Result.failure(NotFoundException())
            items.add(userItem)
        }
        return@withContext Result.success(items)
    }
}