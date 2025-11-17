package com.example.heart2heart.ui.statistic

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.heart2heart.R
import com.example.heart2heart.ui.theme.poppinsFamily
import com.example.heart2heart.ui.theme.ubuntuFamily

@Composable
fun DiagnoseBottomSheet(
    onSubmit: () -> Unit = {},
) {
    val isDark = isSystemInDarkTheme()

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
        Text(
            "Generate Diagnosis",
            fontFamily = ubuntuFamily,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 18.sp,
        )
        Text(
            "AI akan menghasilkan diagnosis dengan data dalam rentang 10 detik terbaru",
            fontFamily = poppinsFamily,
            fontWeight = FontWeight.Normal,
            color = if (isDark) colorResource(R.color.neutral_300)  else colorResource(R.color.neutral_900),
            fontSize = 14.sp,
        )

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = { onSubmit()},
            modifier = Modifier
                .fillMaxWidth()
            ,
            enabled = true,
            shape = RoundedCornerShape(8.dp),
            colors = ButtonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                disabledContentColor = colorResource(R.color.neutral_900),
                disabledContainerColor = colorResource(R.color.neutral_700)
            )
        ) {
            // Button content
//            if (uiState.isLoading) {
//                CircularProgressIndicator(
//                    modifier = Modifier.size(20.dp),
//                    color = MaterialTheme.colorScheme.onPrimary,
//                    strokeWidth = 2.dp
//                )
//            }
            Text(
                text = "Submit",
                modifier = Modifier.padding(vertical = 4.dp),
                fontFamily = ubuntuFamily,
                fontSize = 16.sp,
            )
        }
    }
}