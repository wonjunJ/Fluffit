import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

@Suppress("unused")
internal class FeatureConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("fluffit.plugin.androidlibrary")
                apply("fluffit.plugin.hilt")
                apply("fluffit.plugin.compose")
            }

            extensions.configure<LibraryExtension> {
                defaultConfig {
                    testInstrumentationRunner =
                        "com.google.samples.apps.nowinandroid.core.testing.NiaTestRunner"

                    vectorDrawables {
                        useSupportLibrary = true
                    }
                }
                testOptions.animationsDisabled = true
            }

            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
            dependencies {
                "implementation"(project(":core:domain"))

                "implementation"(libs.findBundle("androidx.runtime").get())
                "implementation"(libs.findBundle("androidx.ui").get())
                "implementation"(libs.findBundle("androidx.room").get())
                "implementation"(libs.findBundle("androidx.navigation").get())

                "implementation"(libs.findLibrary("androidx.appcompat").get())
                "implementation"(libs.findLibrary("androidx.core.ktx").get())

                "implementation"(libs.findLibrary("androidx.lifecycle.runtime.ktx").get())
                "implementation"(libs.findLibrary("androidx.activity.compose").get())
                "implementation"(platform(libs.findLibrary("androidx.compose.bom").get()))
                "implementation"(libs.findLibrary("androidx.material3").get())
                "implementation"(libs.findLibrary("material").get())
                "implementation"(libs.findLibrary("play.services.location").get())
                "implementation"(libs.findLibrary("volley").get())
                "testImplementation"(libs.findLibrary("junit").get())
                "androidTestImplementation"(libs.findLibrary("androidx.junit").get())
                "androidTestImplementation"(libs.findLibrary("androidx.espresso.core").get())
                "androidTestImplementation"(platform(libs.findLibrary("androidx.compose.bom").get()))
                "implementation"(libs.findLibrary("glide").get())
                "ksp"(libs.findLibrary("glide.compiler").get())
                "implementation"("com.navercorp.nid:oauth:5.9.0")
            }
        }
    }
}
