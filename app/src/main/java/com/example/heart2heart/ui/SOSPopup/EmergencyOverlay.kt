package com.example.heart2heart.ui.SOSPopup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.heart2heart.ECGExtraction.data.DetectionType
import com.example.heart2heart.R

@Composable
fun OverlaySOS(
    onDismissRequest: () -> Unit,
    isOpenDialog: Boolean = false,
    timeRemaining: Long = 0L,
    isReceiverDialogOpen: Boolean = false,
    senderOnReceiver: String? = null,
    onDismissReceiverDialog: () -> Unit = {},
) {
    if (isOpenDialog) {
        Dialog(
            onDismissRequest = { },
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false,
                decorFitsSystemWindows = true,
            )
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                ,
                shape = RoundedCornerShape(32.dp),
                colors = CardColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    disabledContentColor = colorResource(R.color.neutral_900),
                    disabledContainerColor = colorResource(R.color.neutral_700)
                )
            ) {
                SOSEmergencyPopup(
                    onFalseAlarmClick = onDismissRequest,
                    remainingTime = timeRemaining
                )
            }
        }
    }
    if (isReceiverDialogOpen && senderOnReceiver != null) {
        Dialog(
            onDismissRequest = { },
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false,
                decorFitsSystemWindows = true,
            )
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                ,
                shape = RoundedCornerShape(32.dp),
                colors = CardColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    disabledContentColor = colorResource(R.color.neutral_900),
                    disabledContainerColor = colorResource(R.color.neutral_700)
                )
            ) {
                SOSEmergencyReceiver(
                    onOkayReceive = onDismissReceiverDialog,
                    userDetected = senderOnReceiver,
                )
            }
        }
    }
}

@Composable
fun OverlayReport(
    onDismissRequest: () -> Unit,
    senderOnReceiver: String? = null,
    isOpenDialog: Boolean = false,
    detectionType: DetectionType? = null,

) {
    if (isOpenDialog && detectionType != null && senderOnReceiver != null) {
        Dialog(
            onDismissRequest = { },
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false,
                decorFitsSystemWindows = true,
            )
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                ,
                shape = RoundedCornerShape(32.dp),
                colors = CardColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    disabledContentColor = colorResource(R.color.neutral_900),
                    disabledContainerColor = colorResource(R.color.neutral_700)
                )
            ) {
                ArrhythmiaDetection(
                    arrhythmiaType = detectionType,
                    userDetected = senderOnReceiver,
                    isNotifier = false,
                    onConfirm = onDismissRequest,
                )
            }
        }
    }
    else if (isOpenDialog && detectionType != null) {
        Dialog(
            onDismissRequest = { },
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false,
                decorFitsSystemWindows = true,
            )
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                ,
                shape = RoundedCornerShape(32.dp),
                colors = CardColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    disabledContentColor = colorResource(R.color.neutral_900),
                    disabledContainerColor = colorResource(R.color.neutral_700)
                )
            ) {
                ArrhythmiaDetection(
                    arrhythmiaType = detectionType,
                    isNotifier = true,
                    onConfirm = onDismissRequest,
                )
            }
        }
    }
}