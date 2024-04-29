package com.kiwa.fluffit.convention

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions

//KotlinAndorid.kt
internal fun Project.configureKotlinAndroid(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
){
    commonExtension.apply {
        compileSdk = CONST_Version.compileSdk
        defaultConfig {
            minSdk = CONST_Version.minSDK
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            vectorDrawables{
                useSupportLibrary = true
            }
        }

        buildTypes {
            named("release"){
                isMinifyEnabled = false
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            }
        }

        compileOptions {
            sourceCompatibility = CONST_Version.JAVA_VERSION
            targetCompatibility = CONST_Version.JAVA_VERSION
        }

        kotlinOptions {
            jvmTarget = CONST_Version.JAVA_VERSION.toString()
        }

        packaging {
            resources {
                excludes += "/META-INF/{AL2.0,LGPL2.1}"
            }
        }
    }
}
internal fun CommonExtension<*, *, *, *, *, *>.kotlinOptions(
    block: KotlinJvmOptions.() -> Unit
){
    (this as ExtensionAware).extensions.configure("kotlinOptions", block)
}