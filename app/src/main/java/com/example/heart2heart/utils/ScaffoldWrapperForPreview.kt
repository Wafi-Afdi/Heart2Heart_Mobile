package com.example.heart2heart.utils

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import com.example.heart2heart.ui.theme.Heart2HeartTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreviewWrapperWithScaffold(content: @Composable (paddingValues: androidx.compose.foundation.layout.PaddingValues) -> Unit) {
    // Note: It's best practice to wrap the Scaffold in your app's Theme
    // MyTheme {
    Heart2HeartTheme {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text("Component Preview") })
            }
        ) { paddingValues ->
            // Execute the provided content, passing the necessary padding
            content(paddingValues)
        }
    }
    // }
}