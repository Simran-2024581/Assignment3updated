plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.gms.google-services")   // <-- Correct
}

android {
    namespace = "com.example.assignment2brewbyte"
    compileSdk = 34  // use stable version, NOT 36

    defaultConfig {
        applicationId = "com.example.assignment2brewbyte"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

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

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    // CameraX
    dependencies {
        // ... other dependencies like core-ktx, appcompat, etc.

        // Add the following CameraX dependencies
        val cameraxVersion = "1.3.4" // Use the latest stable version of CameraX
        implementation("androidx.camera:camera-core:${cameraxVersion}")
        implementation("androidx.camera:camera-camera2:${cameraxVersion}")
        implementation("androidx.camera:camera-lifecycle:${cameraxVersion}")
        implementation("androidx.camera:camera-view:${cameraxVersion}")

        // Firebase BOM
        implementation(platform("com.google.firebase:firebase-bom:33.3.0"))

        // ... other Firebase and Google dependencies
    }

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Firebase BOM
    implementation(platform("com.google.firebase:firebase-bom:33.3.0"))

    // Firebase Auth
    implementation("com.google.firebase:firebase-auth-ktx")

    // Firestore
    implementation("com.google.firebase:firebase-firestore-ktx")

    // Google Sign-In
    implementation("com.google.android.gms:play-services-auth:21.2.0")
    // Google Maps
    implementation("com.google.android.gms:play-services-maps:19.0.0")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

}
