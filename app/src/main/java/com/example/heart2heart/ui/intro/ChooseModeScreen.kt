package com.example.heart2heart.ui.intro

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.heart2heart.ui.intro.components.NavButtonForMode

@Composable
fun ChooseModeScreen(
    getProfile: () -> Unit = {},
    onClickAmbulatory: () -> Unit = {},
    onClickObserver: () -> Unit = {}
) {
    LaunchedEffect(1) {
        getProfile()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        NavButtonForMode(title = "Ambulatory Mode",
            description = "Dalam mode ini anda dapat menghubungkan telepon genggam dengan alat ECG dari " +
            "Tim Capstone untuk mendapatkan informasi kondisi jantung anda",
            onClick = onClickAmbulatory
        )

        Spacer(modifier = Modifier.height(16.dp))

        NavButtonForMode(title = "Observer Mode", description = "Dalam mode ini anda dapat melakukan observasi terhadap user lain mengenai" +
                " kondisi jantungnya, jika alat ambulatory ECG nya hidup dan dia nya terhubung oleh" +
                " internet",
            onClick = onClickObserver
        )

    }
}

@Preview(showBackground = true)
@Composable
fun ChooseModeScreenPreview() {

    val navController = rememberNavController()
    // You can use a Surface to give a background to your preview
    Surface(
        color = Color.White
    ) {
        ChooseModeScreen()
    }
}
