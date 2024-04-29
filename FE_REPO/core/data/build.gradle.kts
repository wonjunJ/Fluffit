plugins {
    alias(libs.plugins.fluffit.androidlibrary)
    alias(libs.plugins.fluffit.hilt)
}

android {
    namespace = "com.kiwa.fluffit.data"
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.hilt.core)
    implementation(project(":core:domain"))
}