plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.newtyf.cnp_patients_app"
    compileSdk = 36 // Se asigna directamente con el signo igual

    defaultConfig {
        applicationId = "com.newtyf.cnp_patients_app"
        minSdk = 24 // Ajusta según tus necesidades
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
    }

    // El error de "release" suele ocurrir si está mal anidado
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    defaultConfig {
        applicationId = "com.newtyf.cnp_patients_app"
        minSdk = 26
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
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation("androidx.core:core-splashscreen:1.0.1")
    implementation("com.airbnb.android:lottie:6.1.0")
// (Verificar la última versión)
}