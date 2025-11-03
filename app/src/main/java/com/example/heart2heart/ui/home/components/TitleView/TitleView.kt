package com.example.heart2heart.ui.home.components.TitleView

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.heart2heart.R
import com.example.heart2heart.ui.theme.poppinsFamily
import com.example.heart2heart.ui.theme.ubuntuFamily
import com.example.heart2heart.utils.SettingsIcon

@Composable
fun TitleView(onClickSetting: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .fillMaxWidth()
        ,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {

            Text(
                "Welcome Back",
                fontFamily = poppinsFamily,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 14.sp,
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                "Wafi Afdi A",
                fontFamily = ubuntuFamily,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                "Currently in ambulatory mode",
                fontFamily = poppinsFamily,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 14.sp,
            )
        }

        IconButton(
            onClick = {
                println("Favorite button clicked!")
                onClickSetting()
            },
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(8.dp)
                )
        ) {
            // The Icon itself is now the correct size (e.g., 24.dp)
            Icon(
                // The default icon size is around 24.dp, which is a good size.
                // The IconButton itself provides the 'padding' (size) for the touch target.
                imageVector = SettingsIcon,
                contentDescription = "Change Mode",
                tint = colorResource(R.color.white),
                modifier = Modifier
                    .size(24.dp)
            )
        }
    }
}