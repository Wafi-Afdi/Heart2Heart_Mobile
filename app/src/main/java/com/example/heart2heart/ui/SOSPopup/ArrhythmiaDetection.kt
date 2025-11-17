package com.example.heart2heart.ui.SOSPopup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.heart2heart.ECGExtraction.data.DetectionType
import com.example.heart2heart.R
import com.example.heart2heart.ui.theme.Heart2HeartTheme
import com.example.heart2heart.ui.theme.ubuntuFamily

@Composable
fun ArrhythmiaDetection(
    arrhythmiaType: DetectionType,
    userDetected: String = "User Name",
    isNotifier: Boolean = false,
    onConfirm: () -> Unit = {}
) {
    val annotatedString = buildAnnotatedString {
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            if (isNotifier) {
                append("You")
            } else {
                append(userDetected)
            }
        }
        if (isNotifier) {
            append(" have ")
        } else {
            append(" has ")
        }
        append(" been detected with ")
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            when(arrhythmiaType) {
                DetectionType.TACHYCARDHIA -> append("Tachycardia")
                DetectionType.BRADYCARDHIA -> append("Bradycardia")
                DetectionType.ASYSTOLE -> append("Asystole")
                DetectionType.VFIB -> append("Ventricular Fibrillation")
                DetectionType.AFIB -> append("Atrial Fibrillation")
                DetectionType.VT -> append("Ventricular Tachycardia")
                else -> append("Unknown")
            }
        }
        append(". Please check ")
        if (isNotifier) {
            append("with the doctor")
        } else {
            append("on ")
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append(userDetected)
            }
            append(" immediately")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.Transparent)
    ) {
        Spacer(Modifier.height(24.dp))
        Box(modifier = Modifier.fillMaxWidth()) {
            Icon(
                painter = painterResource(R.drawable.sos_ic),
                tint = colorResource(R.color.warning),
                contentDescription = "SOS",
                modifier = Modifier.size(120.dp).align(Alignment.Center)
            )
        }
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = annotatedString,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(24.dp))
        Button(
            onClick = onConfirm,
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
            Text(
                text = "Confirm",
                modifier = Modifier.padding(vertical = 4.dp),
                fontFamily = ubuntuFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
            )
        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFFFFFFF, // Optional: dark background to match neutral_700
    showSystemUi = false
)
@Composable
fun PreviewArrhythmia() {
    Heart2HeartTheme {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            ArrhythmiaDetection(
                arrhythmiaType = DetectionType.BRADYCARDHIA,
                isNotifier = false,
            )
        }
    }
}