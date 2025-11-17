package com.example.heart2heart.ui.contact

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.heart2heart.auth.data.AppType
import com.example.heart2heart.contacts.data.ContactUIState
import com.example.heart2heart.contacts.presentation.ContactViewModel
import com.example.heart2heart.ui.contact.component.AddContactForm
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactScreenRoute(
    modifier: Modifier = Modifier,
    contactViewModel: ContactViewModel,
    appType: AppType
) {
    val sheetState = rememberModalBottomSheetState()
    var isSheetOpen by rememberSaveable {
        mutableStateOf(false)
    }

    val listOfContacts by contactViewModel.listOfContacts.collectAsState()
    val isLoadingAddContact by contactViewModel.isLoadingAddContact.collectAsState()

    val isLoadingGetContact by contactViewModel.isLoadingContacts.collectAsState()



    val mappedContacts = remember(listOfContacts) {
        listOfContacts.map { contact ->
            // Your conversion logic goes here
            ContactUIState(
                id = contact.userId.toString(),
                name = contact.name,
                phone = contact.phone,
                email = contact.email
            )
        }
    }


    LaunchedEffect(listOfContacts) {
        contactViewModel.getListOfContacts()
    }

    LaunchedEffect(isLoadingAddContact) {
        if (isSheetOpen && !isLoadingAddContact) {
            isSheetOpen = false
        }
    }



    ContactScreen(modifier = modifier,
        listOfContacts = mappedContacts,
        onFABClick = { isSheetOpen = true},
        removeAccess = {
            it ->
            contactViewModel.deleteContact(UUID.fromString(it))
        },
        reloadList = { contactViewModel.getListOfContacts()},
        isRefreshing = isLoadingGetContact,
    )

    if (isSheetOpen) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = {
                isSheetOpen = false
            },
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            AddContactForm(
                onSubmit = { it ->
                    contactViewModel.addContact(it)
                },
            )
        }
    }
}