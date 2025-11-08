package com.example.heart2heart.home.presentation.state

import com.example.heart2heart.auth.data.AppType
import com.example.heart2heart.auth.data.UserProfile
import com.example.heart2heart.common.domain.model.User

data class UserHomeState(
    val name: String = "",
    val appType: AppType? = null,
    val userBeingMonitored: UserProfile? = null,
)
