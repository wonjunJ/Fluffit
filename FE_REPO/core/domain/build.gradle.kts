plugins {
    alias(libs.plugins.fluffit.javalibrary)
    alias(libs.plugins.ktlintLibrary)
}

dependencies {
    // Inject
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.javax.inject)
    implementation(libs.hilt.core)
    implementation(project(":core:model"))
}
