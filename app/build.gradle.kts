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

    buildFeatures{
        viewBinding = true
        dataBinding = true
    }
}

composeCompiler {
    enableStrongSkippingMode = true

    reportsDestination = layout.buildDirectory.dir("compose_compiler")
}

dependencies {

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.2")
    implementation("junit:junit:4.13.2")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
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

    //SHA-256
    implementation("commons-codec:commons-codec:1.15")

    //Reflect (обращение к именам классов и т.п.) - рефлексия
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.9.20")

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

    //Glide
    implementation("com.github.bumptech.glide:glide:4.16.0")


    // Glide v4 uses this new annotation processor -- see https://bumptech.github.io/glide/doc/generatedapi.html
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")


    // Coil
    implementation("io.coil-kt:coil-compose:2.7.0")

    //ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")

    implementation("androidx.fragment:fragment-ktx:1.8.1")
}