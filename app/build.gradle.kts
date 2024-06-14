plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.example.practiceandroid"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.practiceandroid"
        minSdk = 21
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
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

composeCompiler {
    enableStrongSkippingMode = true

    reportsDestination = layout.buildDirectory.dir("compose_compiler")
}

dependencies {

    implementation("androidx.core:core-ktx:1.13.1")
    implementation(libs.androidx.lifecycle.runtime.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation("androidx.activity:activity-compose:1.9.0")

    //Корутины
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    //ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.2")

    //DataStore
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    //Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")

    //Gson библиотека-конвертер в ретрофит. Подключает GSON от google автоматом
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    //Jetpack compose
    // Основные библиотеки Compose для создания UI
    implementation("androidx.compose.ui:ui:1.6.8")
    // Библиотеки Compose для Material Design
    implementation("androidx.compose.material3:material3:1.2.1")
    // Утилиты для разработки и отладки Compose интерфейсов
    implementation("androidx.compose.ui:ui-tooling:1.6.8")
    // Библиотека для работы Compose с LiveData
    implementation("androidx.compose.runtime:runtime-livedata:1.6.8")
    // Библиотека для работы Compose с ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.2")
    // Навигация в Compose
    implementation("androidx.navigation:navigation-compose:2.7.7")
}