package com.example.heart2heart.ui.auth.Login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.heart2heart.R
import com.example.heart2heart.ui.home.HomeScreen
import com.example.heart2heart.ui.theme.ubuntuFamily
import com.example.heart2heart.utils.Eye
import com.example.heart2heart.utils.EyeSlash
import com.example.heart2heart.utils.Key
import com.example.heart2heart.utils.Mail
import com.example.heart2heart.utils.PreviewWrapperWithScaffold
import com.patrykandpatrick.vico.compose.chart.layout.fullWidth


private const val SIGN_UP_TAG = "SIGN_UP"
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onSignUpClicked: () -> Unit = {},
    onLoginButtonClicked: () -> Unit = {},
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }

    val visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation()

    val annotatedText = buildAnnotatedString {
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onSurface)) {
            append("Don't have an account? ")
        }

        pushStringAnnotation(
            tag = SIGN_UP_TAG,
            annotation = "Sign Up"
        )
        withStyle(
            style = SpanStyle(
                color = colorResource(R.color.primary_light),
                fontWeight = FontWeight.Bold,
                textDecoration = TextDecoration.Underline
            )
        ) {
            append("Sign Up")
        }
        pop()
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(horizontal = 16.dp)
        ,
    ) {
        Spacer(Modifier.height(50.dp))
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
            ,
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            leadingIcon = {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = Mail,
                    contentDescription = "Email icon"
                )
            }
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
            ,
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            leadingIcon = {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = Key,
                    contentDescription = "Password Icon"
                )
            },
            visualTransformation = visualTransformation,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                IconButton(onClick = { showPassword = !showPassword }) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = if (showPassword) EyeSlash else Eye,
                        contentDescription = if (showPassword) "Hide password" else "Show password"
                    )
                }
            },
        )

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = onLoginButtonClicked,
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
                text = "Login",
                modifier = Modifier.padding(vertical = 4.dp),
                fontFamily = ubuntuFamily,
                fontSize = 16.sp,
            )
        }

        Spacer(Modifier.height(16.dp))

        ClickableText(
            text = annotatedText,
            onClick = { offset ->
                annotatedText.getStringAnnotations(
                    tag = SIGN_UP_TAG,
                    start = offset,
                    end = offset
                ).firstOrNull()?.let { annotation ->
                    if (annotation.tag == SIGN_UP_TAG) {
                        onSignUpClicked()
                        println("Sign Up link clicked!")
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
            ,
            style = LocalTextStyle.current.copy(
                textAlign = TextAlign.Center
            ),
        )
    }
}

@Preview(showBackground = true, name = "My Component in a Scaffold")
@Composable
fun MyIsolatedComponentPreview() {
    PreviewWrapperWithScaffold { paddingValues ->
        // Call your isolated component inside the wrapper's content lambda,
        // using the padding provided by the Scaffold.
        LoginScreen(modifier = Modifier.padding(paddingValues))
    }
}