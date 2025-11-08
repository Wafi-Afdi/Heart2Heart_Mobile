package com.example.heart2heart.ui.contact

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.heart2heart.contacts.data.ContactUIState

@Composable
fun ContactScreenRoute(
    modifier: Modifier = Modifier,
) {
    val listPlaceholder = listOf<ContactUIState>(
        ContactUIState(name = "John", phone = "+62 12321-3213-321", email ="John@gmail.com"),
        ContactUIState(name = "John", phone = "+62 12321-3213-321", email ="John@gmail.com")
    )
    ContactScreen(modifier = modifier, listOfContacts = listPlaceholder)
}