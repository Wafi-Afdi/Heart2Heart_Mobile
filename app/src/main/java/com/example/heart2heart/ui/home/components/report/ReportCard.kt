package com.example.heart2heart.ui.home.components.report

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.heart2heart.R
import com.example.heart2heart.ui.theme.Heart2HeartTheme
import com.example.heart2heart.ui.theme.poppinsFamily
import com.example.heart2heart.ui.theme.ubuntuFamily
import com.example.heart2heart.utils.CpuChip
import com.example.heart2heart.utils.ThumbsupFilled
import com.example.heart2heart.utils.TriangleRight

@Composable
fun ReportCard(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Sinusoidal Rhytm",
                    style = LocalTextStyle.current.copy(
                        lineHeight = 16.sp
                    ),
                    fontFamily = ubuntuFamily,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 14.sp,
                )
                Spacer(Modifier.width(4.dp))
                Icon(
                    imageVector = ThumbsupFilled,
                    contentDescription = "Ok Result",
                    tint = colorResource(R.color.success),
                    modifier = Modifier.size(16 .dp)
                )
            }

            Text(
                text = "Thu, 14 Sep 15:36",
                style = LocalTextStyle.current.copy(
                    lineHeight = 16.sp
                ),
                fontFamily = ubuntuFamily,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 14.sp,
            )
        }

        Spacer(Modifier.height(4.dp))

        Text(
            text = "Descriptive text regarding the disease and other - other",
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            style = LocalTextStyle.current.copy(
                lineHeight = 16.sp
            ),
            fontFamily = poppinsFamily,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 14.sp,
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "More",
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                style = LocalTextStyle.current.copy(
                    lineHeight = 16.sp
                ),
                fontFamily = poppinsFamily,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 14.sp,
            )
            Spacer(Modifier.width(4.dp))
            Icon(
                imageVector = TriangleRight,
                contentDescription = "More",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(16 .dp)
            )
        }
    }
    Spacer(Modifier.height(4.dp))
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFF121212, // Optional: dark background to match neutral_700
    showSystemUi = false
)
@Composable
fun ReportCardPreview() {
    // If you have a theme, wrap it here
    Heart2HeartTheme {
        ReportCard(
            modifier = Modifier
                .padding(16.dp)
        )
    }
}