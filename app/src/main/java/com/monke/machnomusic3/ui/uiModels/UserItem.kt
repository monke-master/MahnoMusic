package com.monke.machnomusic3.ui.uiModels

import com.monke.machnomusic3.domain.model.User

data class UserItem(
    val user: User,
    val profilePicUrl: String?
)