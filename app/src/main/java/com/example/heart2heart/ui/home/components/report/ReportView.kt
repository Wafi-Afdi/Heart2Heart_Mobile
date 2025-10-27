package com.example.heart2heart.ui.home.components.report

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.heart2heart.R
import com.example.heart2heart.ui.theme.poppinsFamily
import com.example.heart2heart.ui.theme.ubuntuFamily

@Composable
fun ReportView() {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 0.dp)
            .fillMaxWidth()
        ,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Report",
                fontFamily = poppinsFamily,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.primary_light),
                fontSize = 16.sp,
            )

            Text(
                "See more",
                fontFamily = poppinsFamily,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.primary_light),
                fontSize = 12.sp,
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            ReportCard()
            ReportCard()
            ReportCard()
        }
        /*Button(
            onClick = { },
            modifier = Modifier
                .fillMaxWidth()
            ,
            shape = RoundedCornerShape(8.dp),
            colors = ButtonColors(
                containerColor = colorResource(R.color.primary_light),
                contentColor = colorResource(R.color.text_dark),
                disabledContentColor = colorResource(R.color.neutral_900),
                disabledContainerColor = colorResource(R.color.neutral_700)
            )
        ) {
            // Button content
            Text(
                text = "Load More",
                modifier = Modifier.padding(vertical = 4.dp),
                fontFamily = ubuntuFamily,
                fontSize = 16.sp,
            )
        }*/
    }
}