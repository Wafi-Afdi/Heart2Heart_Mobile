package com.example.heart2heart.utils

import kotlinx.serialization.Serializable

@Serializable
object AuthRoute

@Serializable
object LoginRoute

@Serializable
object AuthCheckRoute

@Serializable
object SignUpRoute

@Serializable
object ChooseModeScreenRoute
@Serializable
object ChooseWhoToObserveRoute


@Serializable
object MainScreenRoute

@Serializable
object HomeScreenRoute
@Serializable
object StatisticsRoute
@Serializable
object ContactsRoute
@Serializable
object SettingsRoute
@Serializable
object MapScreenRoute


@Serializable
data class ReportResultScreenRoute(val reportId: String)
