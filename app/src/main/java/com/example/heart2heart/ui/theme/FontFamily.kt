package com.example.heart2heart.ui.theme

import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import com.example.heart2heart.R

private val ubuntuFont = GoogleFont("Ubuntu")
private val poppinFont = GoogleFont("Poppins")

val googleFontProvider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val ubuntuFamily = FontFamily(
    Font(googleFont = ubuntuFont, fontProvider = googleFontProvider)
)

val poppinsFamily = FontFamily(
    Font(googleFont = poppinFont, fontProvider = googleFontProvider)
)



