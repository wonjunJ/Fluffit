import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.fluffit.hilt)
    id("kotlin-kapt")
}

val properties = Properties().apply {
    load(project.rootProject.file("local.properties").inputStream())
}

android {
    namespace = "com.kiwa.fluffit"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.kiwa.fluffit"
        minSdk = 30
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
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

    defaultConfig {
        buildConfigField(
            "String",
            "BASE_URL",
            "\"${properties["BASE_URL"]}\""
        )
    }

    configurations.implementation {
        exclude(group = "com.intellij", module = "annotations")
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    kapt {
        correctErrorTypes = true
        useBuildCache = false
    }
}

dependencies {
    implementation(libs.play.services.wearable)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.coil.compose)
    implementation(libs.coil.gif)
    implementation(libs.retrofitGson)
    implementation(project(":core:base"))
    implementation(project(":core:domain"))
    implementation(project(":core:model"))
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.retrofitGson)
    implementation(libs.coil)
    implementation(libs.okhttp.sse)
    implementation(libs.okhttpLogging)

//    implementation(libs.hilt.android)
//    kapt(libs.hilt.compiler)

    implementation("androidx.health:health-services-client:1.1.0-alpha02")
    implementation("com.google.android.horologist:horologist-compose-layout:0.5.26")
    wearApp(project(":wear"))
}
