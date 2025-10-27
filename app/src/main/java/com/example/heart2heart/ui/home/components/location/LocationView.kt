package com.example.heart2heart.ui.home.components.location

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.heart2heart.R
import com.example.heart2heart.ui.theme.poppinsFamily
import com.example.heart2heart.ui.theme.ubuntuFamily
import com.example.heart2heart.utils.Location_on

@Composable
fun LocationView(modifier: Modifier = Modifier, lat: Double = 0.0, long: Double = 0.0, provinceState: String = "Jakarta", nation: String = "Indonesia") {

    val latituteAndLongitute = buildAnnotatedString {
        append("Lat: ")

        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append(String.format("%.2f", lat))
        }

        append("\n")

        append("Long: ")

        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append(String.format("%.2f", long))
        }
    }

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(colorResource(R.color.primary_light))
            .padding(12.dp)
            .width(10.dp)
            .height(100.dp)
        ,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Location_on,
            contentDescription = "Location Icon",
            tint = colorResource(R.color.white),
            modifier = Modifier.size(36.dp)
        )
        Spacer(modifier = Modifier.width(2.dp))
        Column {
            Text(
                text = nation,
                fontWeight = FontWeight.Bold,
                style = LocalTextStyle.current.copy(
                    lineHeight = 16.sp // adjust as needed, try 16–20.sp
                ),
                fontFamily = ubuntuFamily,
                color = colorResource(R.color.text_dark),
                fontSize = 14.sp
            )
            Text(
                text = provinceState,
                style = LocalTextStyle.current.copy(
                    lineHeight = 16.sp // adjust as needed, try 16–20.sp
                ),
                fontWeight = FontWeight.Bold,
                fontFamily = ubuntuFamily,
                color = colorResource(R.color.text_dark),
                fontSize = 14.sp
            )
            Text(
                text = latituteAndLongitute,
                style = LocalTextStyle.current.copy(
                    lineHeight = 16.sp // adjust as needed, try 16–20.sp
                ),
                fontFamily = ubuntuFamily,
                color = colorResource(R.color.text_dark),
                fontSize = 14.sp
            )
        }
    }
}