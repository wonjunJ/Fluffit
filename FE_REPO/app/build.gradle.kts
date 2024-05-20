import java.util.Properties

plugins {
    alias(libs.plugins.fluffit.application)
    alias(libs.plugins.fluffit.hilt)
    alias(libs.plugins.ktlintLibrary)
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

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
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
}

dependencies {
    implementation(libs.bundles.androidx.runtime)
    implementation(libs.bundles.androidx.ui)
    implementation(libs.bundles.androidx.room)
    implementation(libs.bundles.androidx.navigation)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.material3)
    implementation(libs.play.services.location)

    implementation(libs.androidx.appcompat)
    implementation(libs.play.services.wearable)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(project(":core:designsystem"))
    implementation(project(":core:base"))

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.retrofit)
    implementation(libs.retrofitGson)

    implementation(libs.okhttp)
    implementation(libs.okhttpLogging)

    implementation("com.navercorp.nid:oauth:5.9.1") // jdk 11

    wearApp(project(":wear"))
    implementation(project(":core:domain"))
    implementation(project(":core:data"))
    implementation(project(":core:model"))
    implementation(project(":feature:login"))
    implementation(project(":feature:battle-record"))
    implementation(project(":feature:collection"))
    implementation(project(":feature:flupet-history"))
    implementation(project(":feature:home"))
    implementation(project(":feature:mypage"))
    implementation(project(":feature:ranking"))
}
