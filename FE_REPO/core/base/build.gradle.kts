plugins {
    alias(libs.plugins.fluffit.feature)
    alias(libs.plugins.ktlintLibrary)
    id("kotlin-kapt")
}

android {
    namespace = "com.kiwa.fluffit.base"
}
configurations.implementation {
    exclude(group = "com.intellij", module = "annotations")
}
