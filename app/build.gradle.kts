import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
    alias(libs.plugins.kotlin.serialization)

    id("com.google.gms.google-services")
}

val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(FileInputStream(localPropertiesFile))
}

val beApiUrl = localProperties.getProperty("Heart2Heart.BE_API_LOCAL") // Fallback to local.properties
    ?: "http:127.0.0.1"

val wsApiUrl = localProperties.getProperty("Heart2Heart.WS_API") // Fallback to local.properties
    ?: "http:127.0.0.1"

val baseDomain = localProperties.getProperty("Heart2Heart.DOMAIN_BE") // Fallback to local.properties
    ?: "http:127.0.0.1"
val mapsApiKey = localProperties.getProperty("MAPS_API_KEY") // Fallback to local.properties
    ?: "http:127.0.0.1"

android {
    namespace = "com.example.heart2heart"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.heart2heart"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        buildConfigField("String", "BE_API_LOCAL", "\"$beApiUrl\"")
        buildConfigField("String", "WS_API", "\"$wsApiUrl\"")
        buildConfigField("String", "DOMAIN_BE", "\"$baseDomain\"")
        manifestPlaceholders["mapsApiKey"] = mapsApiKey

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_17
        }
    }



    buildFeatures {
        compose = true
        buildConfig = true

    }
}

room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.material3)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.firebase.messaging.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation("androidx.compose.ui:ui-text-google-fonts:1.5.4")
    implementation("androidx.navigation:navigation-compose:2.9.0")

    // view model
    implementation(libs.androidx.lifecycle.viewmodel)

    // HTTP Request
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.retrofit2:converter-scalars:2.9.0")

    // Arrow
    implementation("io.arrow-kt:arrow-core:1.2.0")
    implementation("io.arrow-kt:arrow-fx-coroutines:1.2.0")

    // Charts
    implementation("com.patrykandpatrick.vico:compose:1.15.0")
    implementation("com.patrykandpatrick.vico:compose-m3:1.15.0") // For Material 3
    implementation("com.patrykandpatrick.vico:core:1.15.0")

    // Maps
    implementation("com.google.maps.android:maps-compose:6.5.3")
    implementation("com.google.android.gms:play-services-location:21.0.1")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:34.5.0"))

    // Splash string
    implementation("androidx.core:core-splashscreen:1.0.1")

    implementation(libs.kotlinx.serialization.json)

    // dagger
    implementation(libs.hilt)
    implementation(libs.androidx.hilt.viewmodel)
    implementation(libs.androidx.hilt.navigation)
    ksp(libs.hilt.compiler)
    ksp(libs.androidx.hilt.compiler)

    // Room
    ksp(libs.room.compiler)
    implementation(libs.room.runtime)
    annotationProcessor(libs.room.compiler)
    implementation(libs.room.ktx)
}