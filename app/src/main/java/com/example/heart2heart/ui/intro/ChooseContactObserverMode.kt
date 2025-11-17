package com.example.heart2heart.ui.intro

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.heart2heart.auth.data.UserProfile
import com.example.heart2heart.contacts.data.ContactUIState
import com.example.heart2heart.ui.contact.AddContactFAB
import com.example.heart2heart.ui.contact.component.ContactCard
import com.example.heart2heart.ui.intro.components.ChooseContactCard
import com.example.heart2heart.ui.intro.components.ChooseModeScreenViewModel
import com.example.heart2heart.ui.theme.poppinsFamily
import com.patrykandpatrick.vico.compose.chart.layout.fullWidth
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.UUID

@Composable
fun ChooseContactObserverMode(
    modifier: Modifier = Modifier,
    viewModel: ChooseModeScreenViewModel,
    onChooseWhoToObserve: () -> Unit = { }
) {

    val scrollState = rememberScrollState()
    val listOfContact by viewModel.listOfContacts.collectAsState()
    val isLoadingContact by viewModel.isLoadingContacts.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getListOfContacts()
    }
    PullToRefreshBox(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        isRefreshing = isLoadingContact,
        onRefresh = { viewModel.getListOfContacts() }
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            Text(
                "Choose who to observe",
                fontFamily = poppinsFamily,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 16.sp,
            )
            Spacer(Modifier.height(8.dp))
            if(listOfContact.size == 0) {
                Text(
                    "Nobody have given you access",
                    fontFamily = poppinsFamily,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            listOfContact.forEach { contactUIState ->
                ChooseContactCard(
                    name = contactUIState.name,
                    phone = contactUIState.phone,
                    email = contactUIState.email,
                    onClickCard = {
                        viewModel.setEcgObserving(
                            UserProfile(
                                name = contactUIState.name,
                                phoneNumber = contactUIState.phone,
                                email = contactUIState.email,
                                id = contactUIState.userId.toString()
                            )
                        )
                        onChooseWhoToObserve()
                    }
                )
                Spacer(Modifier.height(4.dp))
            }

        }
    }
}