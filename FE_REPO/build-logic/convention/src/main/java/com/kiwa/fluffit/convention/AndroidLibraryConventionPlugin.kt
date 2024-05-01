import com.android.build.gradle.LibraryExtension
import com.kiwa.fluffit.convention.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import java.util.Properties

@Suppress("unused")
internal class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
                apply("kotlin-parcelize")
            }

            val properties = Properties().apply {
                load(project.rootProject.file("local.properties").inputStream())
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)

                defaultConfig.targetSdk = 33

                defaultConfig {
                    testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
                    vectorDrawables.useSupportLibrary = true

                    buildConfigField(
                        "String",
                        "BASE_URL",
                        "\"${properties["BASE_URL"]}\""
                    )

                    buildConfigField(
                        "String",
                        "NAVER_LOGIN_CLIENT_ID",
                        "\"${properties["NAVER_LOGIN_CLIENT_ID"]}\""
                    )
                    buildConfigField(
                        "String",
                        "NAVER_LOGIN_CLIENT_SECRET",
                        "\"${properties["NAVER_LOGIN_CLIENT_SECRET"]}\""
                    )

                    buildConfigField(
                        "String",
                        "NAVER_LOGIN_CLIENT_NAME",
                        "\"${properties["NAVER_LOGIN_CLIENT_NAME"]}\""
                    )
                }

                viewBinding.enable = true

                buildTypes {
                    getByName("debug") {
                        isMinifyEnabled = true
                        proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
                    }
                }

                buildFeatures{
                    buildConfig = true
                }
            }

            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
            dependencies {
                "testImplementation"(libs.findLibrary("junit").get())
                "androidTestImplementation"(libs.findLibrary("androidx.junit").get())
                "androidTestImplementation"(libs.findLibrary("androidx.espresso.core").get())
            }
        }
    }
}
