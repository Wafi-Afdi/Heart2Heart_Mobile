package com.example.heart2heart.ui.contact

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.heart2heart.contacts.data.ContactUIState
import com.example.heart2heart.ui.contact.component.ContactCard
import com.example.heart2heart.ui.contact.component.ContactCardPreview
import com.example.heart2heart.ui.theme.poppinsFamily
import com.example.heart2heart.utils.PreviewWrapperWithScaffold

@Composable
fun ContactScreen(
    modifier: Modifier = Modifier,
    listOfContacts: List<ContactUIState> = emptyList<ContactUIState>()
) {
    val scrollState = rememberScrollState()
    Box(modifier = modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 16.dp)) {
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            Text(
                "Contacts",
                fontFamily = poppinsFamily,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 16.sp,
            )
            Spacer(Modifier.height(8.dp))

            listOfContacts.forEach { contactUIState ->
                ContactCard(
                    name = contactUIState.name,
                    phone = contactUIState.phone,
                    email = contactUIState.email
                )
                Spacer(Modifier.height(4.dp))
            }
        }
    }
}

@Preview(showBackground = true, name = "My Component in a Scaffold")
@Composable
fun PreviewContactScreen() {
    val listPlaceholder = listOf<ContactUIState>(
        ContactUIState(name = "John", phone = "+62 12321-3213-321", email ="John@gmail.com"),
        ContactUIState(name = "John", phone = "+62 12321-3213-321", email ="John@gmail.com")
    )
    PreviewWrapperWithScaffold {
        innerPadding ->
        ContactScreen(modifier = Modifier.padding(innerPadding), listOfContacts = listPlaceholder)
    }
}
