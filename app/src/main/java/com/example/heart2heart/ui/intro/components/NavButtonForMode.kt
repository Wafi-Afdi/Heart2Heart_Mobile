package com.example.heart2heart.ui.intro.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.heart2heart.R
import com.example.heart2heart.ui.theme.poppinsFamily
import com.example.heart2heart.ui.theme.ubuntuFamily

@Composable
fun NavButtonForMode(
    title: String = "Observer Mode",
    description: String = "-",
    onClick: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .clip(RoundedCornerShape(12.dp)) // Apply rounded corners
            .background(Color.LightGray) // Set a background color
            .padding(8.dp)
            ,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            title,
            style = TextStyle(
                color = colorResource(R.color.primary_light),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = ubuntuFamily,
            )
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            description,
            style = TextStyle(
                color = colorResource(R.color.black),
                fontSize = 14.sp,
                fontFamily = poppinsFamily,
            )
        )
    }
}