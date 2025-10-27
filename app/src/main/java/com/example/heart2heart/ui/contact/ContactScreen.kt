package com.example.heart2heart.ui.contact

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun ContactScreen() {
    Box(modifier = Modifier.fillMaxSize().background(Color.Yellow), contentAlignment = Alignment.Center) {
        Text(
            text = "Hello Contact!",
        )
    }
}