plugins {
    alias(libs.plugins.fluffit.javalibrary)
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.hilt.core)
    implementation(project(":core:domain"))
}