package com.kiwa.fluffit.convention

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

//Project gradle 단위에서 선언하던 Plugin 중 libs.plugins.androidApplication 과 libs.plugins.jetbrainsKotlinAndroid 을 따로 선언
@Suppress("unused")
internal class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager){
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<ApplicationExtension>{
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = CONST_Version.targetSDK
            }
        }
    }
}