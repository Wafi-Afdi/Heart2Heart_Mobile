package com.example.heart2heart.ui.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.heart2heart.R
import com.example.heart2heart.ui.theme.ubuntuFamily

@Composable
fun SettingScreen(
    modifier: Modifier,
    onLogoutClicked: () -> Unit = {},
) {
    Box(modifier = modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center) {
        Button(
            onClick = onLogoutClicked,

            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
            ,
            shape = RoundedCornerShape(8.dp),
            colors = ButtonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                disabledContentColor = colorResource(R.color.neutral_900),
                disabledContainerColor = colorResource(R.color.neutral_700)
            )
        ) {
                // Button content
                Text(
                    text = "Logout",
                    modifier = Modifier.padding(vertical = 4.dp),
                    fontFamily = ubuntuFamily,
                    fontSize = 16.sp,
                )
        }
    }
}