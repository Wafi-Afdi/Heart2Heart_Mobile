package com.example.heart2heart.ui.fab

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.heart2heart.R
import com.example.heart2heart.utils.ContactsRoute

@Composable
fun FABTransition(
    route: String?,
    onFABClick: () -> Unit = { },
) {
    var isShowContactAdd by remember { mutableStateOf(false) }

    LaunchedEffect(route) {
        if (route == ContactsRoute::class.qualifiedName) {
            isShowContactAdd = true
        }
        else {
            isShowContactAdd = false
        }
    }

    AnimatedVisibility(
        visible = isShowContactAdd,
        enter = slideInVertically(initialOffsetY = { fullHeight -> fullHeight / 2 }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { fullHeight -> fullHeight / 2 }) + fadeOut(),

        modifier = Modifier.padding(0.dp),
    ) {
        FloatingActionButton(
            onClick = onFABClick,
            shape = RoundedCornerShape(10),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
        ) {
            Icon(
                modifier = Modifier.size(32 .dp),
                painter = painterResource(
                    id = R.drawable.group_add
                ),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}