import com.android.build.api.dsl.ApplicationExtension
import com.kiwa.fluffit.convention.CONST_Version
import com.kiwa.fluffit.convention.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import java.util.Properties

//Project gradle 단위에서 선언하던 Plugin 중 libs.plugins.androidApplication 과 libs.plugins.jetbrainsKotlinAndroid 을 따로 선언
@Suppress("unused")
internal class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager){
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
            }

            val properties = Properties().apply {
                load(project.rootProject.file("local.properties").inputStream())
            }

            extensions.configure<ApplicationExtension>{
                configureKotlinAndroid(this)
                defaultConfig {
                    targetSdk = CONST_Version.targetSDK
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
            }
        }
    }
}
