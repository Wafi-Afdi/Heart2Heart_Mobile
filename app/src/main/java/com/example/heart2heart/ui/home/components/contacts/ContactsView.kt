package com.example.heart2heart.ui.home.components.contacts

import android.R.attr.maxLines
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.heart2heart.R
import com.example.heart2heart.ui.theme.ubuntuFamily
import com.example.heart2heart.websocket.data.dto.UserWebSocketDTO

    @Composable
    fun ContactView(
        modifier: Modifier,
        totalPerson: Int = 0,
        userConnected: List<UserWebSocketDTO> = emptyList()
    ) {

        val connectedNamesString: String = userConnected.joinToString(separator = ", ") { it.name }

        Column (
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(12.dp)
            .width(10.dp)
            .height(100.dp)
            .padding(4.dp)
        ,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "$totalPerson People are monitoring right now",
            style = LocalTextStyle.current.copy(
                lineHeight = 16.sp // adjust as needed, try 16–20.sp
            ),
            fontFamily = ubuntuFamily,
            color = MaterialTheme.colorScheme.primary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )

        Column {
            Text(
                text = connectedNamesString,
                style = LocalTextStyle.current.copy(
                    lineHeight = 13.sp // adjust as needed, try 16–20.sp
                ),
                fontFamily = ubuntuFamily,
                maxLines = 2,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}