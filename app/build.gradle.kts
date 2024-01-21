
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.gl4tp.chatzy"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.gl4tp.chatzy"
        minSdk = 24
        targetSdk = 33
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures{
        viewBinding= true}

}

dependencies {


    //classpath("com.android.tools.build:gradle:7.2.0")
    //classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.10")
    // ... other dependencies ...
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.3.0")

    // splash screen
    implementation("androidx.core:core-splashscreen:1.0.1")

    //lottie animation
    implementation ("com.airbnb.android:lottie:6.0.0")

    //multiwave header
    implementation("com.scwang.wave:MultiWaveHeader:1.0.0-andx")

    // navigation
    val nav_version = "2.5.2"
    //classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.7.6")
    implementation("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation("androidx.navigation:navigation-ui-ktx:$nav_version")

    implementation ("com.google.android.material:material:1.5.0")

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    val room_version = "2.6.1"

    implementation("androidx.room:room-runtime:$room_version")

    // optional - Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:$room_version")
    // To use Kotlin Symbol Processing (KSP)
    ksp("androidx.room:room-compiler:$room_version")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}