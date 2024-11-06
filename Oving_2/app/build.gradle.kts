plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.oving_2"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.oving_2"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            // Disables code shrinking for the release build type.
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        // Sets Java compatibility to version 1.8.
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        // Sets the JVM target to version 1.8.
        jvmTarget = "1.8"
    }

    buildFeatures {
        // Enables Jetpack Compose for the project.
        compose = true
    }

    composeOptions {
        // Specifies the Kotlin compiler extension version for Compose.
        kotlinCompilerExtensionVersion = "1.5.1"
    }

    packaging {
        resources {
            // Excludes certain metadata files to prevent build conflicts.
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Core Android KTX library
    implementation("androidx.core:core-ktx:1.12.0")

    // Lifecycle components
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")

    // Activity KTX for Compose
    implementation("androidx.activity:activity-compose:1.8.0")

    // Jetpack Compose BOM (Bill of Materials)
    implementation(platform("androidx.compose:compose-bom:2023.09.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")

    // AppCompat for backward compatibility
    implementation("androidx.appcompat:appcompat:1.6.1")

    // Material Design components (optional, if needed)
    implementation("com.google.android.material:material:1.10.0")

    // Testing dependencies
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Compose testing dependencies
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.09.01"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}
