package com.example.heart2heart.ui.contact.component

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.heart2heart.R
import com.example.heart2heart.ui.theme.poppinsFamily
import com.example.heart2heart.ui.theme.ubuntuFamily
import com.example.heart2heart.utils.Eye
import com.example.heart2heart.utils.EyeSlash
import com.example.heart2heart.utils.Key
import com.example.heart2heart.utils.Mail
import com.example.heart2heart.utils.PersonCircle

@Composable
fun AddContactForm(
    onSubmit: (email: String) -> Unit = { },
) {
    var emailTarget by remember { mutableStateOf("") }

    val isDark = isSystemInDarkTheme()
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
        Text(
            "Add new contacts",
            fontFamily = ubuntuFamily,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 22.sp,
        )
        Text(
            "People you add will have access to your location, ecg data, and reports",
            fontFamily = poppinsFamily,
            fontWeight = FontWeight.Normal,
            color = if (isDark) colorResource(R.color.neutral_300)  else colorResource(R.color.neutral_900),
            fontSize = 14.sp,
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
            ,
            value = emailTarget,
            onValueChange = { it -> emailTarget = it},
            label = { Text("Email") },
            leadingIcon = {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = Mail,
                    contentDescription = "Email"
                )
            },
        )

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = { onSubmit(emailTarget)},
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
//            if (uiState.isLoading) {
//                CircularProgressIndicator(
//                    modifier = Modifier.size(20.dp),
//                    color = MaterialTheme.colorScheme.onPrimary,
//                    strokeWidth = 2.dp
//                )
//            }
            Text(
                text = "Submit",
                modifier = Modifier.padding(vertical = 4.dp),
                fontFamily = ubuntuFamily,
                fontSize = 16.sp,
            )
        }
    }
}