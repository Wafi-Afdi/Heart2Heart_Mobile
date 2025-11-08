package com.example.heart2heart.ui.contact.component

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.heart2heart.ui.theme.Heart2HeartTheme
import com.example.heart2heart.ui.theme.poppinsFamily
import com.example.heart2heart.ui.theme.ubuntuFamily
import com.example.heart2heart.R
import com.example.heart2heart.utils.PersonCircle

@Composable
fun ContactCard(
    modifier: Modifier = Modifier,
    name: String = "Unknown",
    phone: String = "+62 XXXX-XXX-XXXX",
    email: String = "placeholder@gmail.com",
    onRemoveAccess: () -> Unit = { }
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
        ,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier,
        ) {
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = PersonCircle,
                    contentDescription = "Person",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(16 .dp)
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = name,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = LocalTextStyle.current.copy(
                        lineHeight = 16.sp
                    ),
                    fontFamily = ubuntuFamily,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 16.sp,
                )
            }
            Spacer(Modifier.height(4.dp))
            Text(
                text = email,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = LocalTextStyle.current.copy(
                    lineHeight = 16.sp
                ),
                fontFamily = poppinsFamily,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 14.sp,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = phone,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = LocalTextStyle.current.copy(
                    lineHeight = 16.sp
                ),
                fontFamily = poppinsFamily,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 14.sp,
            )
        }
        IconButton(onClick = onRemoveAccess) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(R.drawable.baseline_group_remove_24),
                contentDescription = "Remove Access",
                tint = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFF121212, // Optional: dark background to match neutral_700
    showSystemUi = false
)
@Composable
fun ContactCardPreview() {
    // If you have a theme, wrap it here
    Heart2HeartTheme {
        ContactCard(
            modifier = Modifier
                .padding(16.dp)
        )
    }
}