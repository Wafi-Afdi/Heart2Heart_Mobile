package com.example.heart2heart.ui.navigation

import androidx.annotation.DrawableRes

data class Screen(
    val route: Any,
    @DrawableRes val icon: Int,
    @DrawableRes val selectedIcon: Int,
    @DrawableRes val altIcon: Int? = null,
)
