import java.util.Properties

plugins {
    alias(libs.plugins.fluffit.androidlibrary)
    alias(libs.plugins.fluffit.hilt)
}

val properties = Properties().apply {
    load(project.rootProject.file("local.properties").inputStream())
}

android {
    namespace = "com.kiwa.fluffit.data"

}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.hilt.core)

    implementation(libs.retrofit)
    implementation(libs.retrofitGson)
    implementation(libs.okhttp)
    implementation(libs.okhttpLogging)

    implementation("androidx.media3:media3-ui:1.3.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")

    implementation(project(":core:domain"))
    implementation(project(":core:model"))
}
