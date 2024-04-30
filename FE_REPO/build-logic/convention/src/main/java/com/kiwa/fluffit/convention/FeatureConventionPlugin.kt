import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

@Suppress("unused")
internal class FeatureConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("fluffit.plugin.androidlibrary")
//                apply("androidx.navigation.safeargs.kotlin")
                apply("fluffit.plugin.hilt")
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
//                "androidTestImplementation"(libs.findLibrary("androidx.ui.test.junit4").get())
                "implementation"(libs.findLibrary("glide").get())
                "ksp"(libs.findLibrary("glide.compiler").get())
                "implementation"(libs.findLibrary("coil").get())
                "implementation"(libs.findLibrary("coil.gif").get())

                "implementation"(libs.findBundle("androidx.lifecycle").get())
            }
        }
    }
}