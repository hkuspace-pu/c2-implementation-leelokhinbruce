plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.adminreservationmanagementapp"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.adminreservationmanagementapp"
        minSdk = 24
        targetSdk = 36
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    // Enable ViewBinding
    buildFeatures {
        viewBinding = true;
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(project(":restaurant_reservation_lib"))

    // ImagePicker (image loading)
    implementation("com.github.dhaval2404:imagepicker:2.1")
    implementation("com.github.bumptech.glide:glide:4.16.0")  // Glide image upload structure

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}