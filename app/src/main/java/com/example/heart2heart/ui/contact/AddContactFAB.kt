package com.example.heart2heart.ui.contact

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
fun AddContactFAB(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = { }
) {

    var isShowContactAdd by remember { mutableStateOf(true) }
    AnimatedVisibility(
        visible = isShowContactAdd,
        enter = slideInVertically(initialOffsetY = { fullHeight -> fullHeight / 2 }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { fullHeight -> fullHeight / 2 }) + fadeOut(),

        modifier = modifier.padding(0.dp),
    ) {
        FloatingActionButton(
            onClick = onClick,
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