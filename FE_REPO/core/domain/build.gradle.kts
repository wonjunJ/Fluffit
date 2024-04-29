plugins {
    alias(libs.plugins.fluffit.javalibrary)
    alias(libs.plugins.ktlintLibrary)
}

dependencies {
    // Inject
    implementation(libs.javax.inject)
    implementation(libs.hilt.core)
}