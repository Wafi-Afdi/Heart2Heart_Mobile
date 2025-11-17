package com.example.heart2heart.auth.domain.model

import java.util.UUID

data class ContactModel(
    val userId: UUID,
    val name: String,
    val email: String,
    val phone: String,
)


data class ListOfContactModelResponse(
    val contactList: List<ContactModel>
)
