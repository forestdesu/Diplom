plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    kotlin("plugin.serialization") version "1.9.0"
}

android {
    namespace = "com.example.diplom"
    compileSdk = 34

    val key: String = com.android.build.gradle.internal.cxx.configure.gradleLocalProperties(rootDir, providers)
        .getProperty("supabaseKey")
    val url: String = com.android.build.gradle.internal.cxx.configure.gradleLocalProperties(rootDir, providers)
        .getProperty("supabaseUrl")

    defaultConfig {
        applicationId = "com.example.diplom"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "supabaseKey", "\"$key\"")
        buildConfigField("String", "supabaseUrl", "\"$url\"")
    }

    sourceSets.getByName("main") {
        java.srcDirs("res/gradients")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.gridlayout)
    implementation(libs.androidx.room.common)
    implementation(libs.androidx.ui.android)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(platform("io.github.jan-tennert.supabase:bom:2.2.3"))
    implementation("io.github.jan-tennert.supabase:postgrest-kt")
    implementation("io.github.jan-tennert.supabase:gotrue-kt:2.3.1")
    implementation("io.ktor:ktor-client-android:2.3.9")
    implementation("com.auth0.android:jwtdecode:2.0.0")
    implementation("me.relex:circleindicator:2.1.6")
    implementation("androidx.viewpager2:viewpager2:1.0.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.3.5")
    implementation("androidx.fragment:fragment-ktx:1.3.2")
    implementation("com.squareup.picasso:picasso:2.8")
    implementation("com.google.android.material:material:1.9.0")
    implementation("pl.droidsonroids.gif:android-gif-drawable:1.2.28")
    implementation("app.futured.donut:donut:2.2.3")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.3.2")
    implementation("it.xabaras.android:recyclerview-swipedecorator:1.4")
}