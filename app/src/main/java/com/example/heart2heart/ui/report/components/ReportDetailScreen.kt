package com.example.heart2heart.ui.report.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.heart2heart.ui.theme.poppinsFamily
import com.example.heart2heart.ui.theme.ubuntuFamily

@Composable
fun ReportDetailComponent(
    title: String = "Ventricular Tachycardia",
    description: String = VentricularTachyDescription
) {
    Column(
        Modifier.fillMaxWidth()
    ) {

        Text(
            text = title,
            fontFamily = ubuntuFamily,
            color = MaterialTheme.colorScheme.primary,
            fontSize = 16.sp,
            lineHeight = 18.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = description,
            fontFamily = poppinsFamily,
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 12.sp,
            lineHeight = 18.sp,
            textAlign = TextAlign.Justify
        )
    }
}