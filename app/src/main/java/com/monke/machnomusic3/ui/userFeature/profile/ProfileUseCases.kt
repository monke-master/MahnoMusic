package com.monke.machnomusic3.ui.userFeature.profile

import com.monke.machnomusic3.domain.usecase.user.GetUserUseCase
import javax.inject.Inject

data class ProfileUseCases @Inject constructor(
    val getUserUseCase: GetUserUseCase
)