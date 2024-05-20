plugins {
    alias(libs.plugins.fluffit.feature)
    alias(libs.plugins.ktlintLibrary)
    id("kotlin-kapt")
}

android {
    namespace = "com.kiwa.fluffit.flupet_history"

    configurations.implementation {
        exclude(group = "com.intellij", module = "annotations")
    }
}
dependencies {
    implementation("com.navercorp.nid:oauth:5.9.1")
    implementation(project(":feature:home"))
    implementation(project(":core:domain"))
    implementation(project(":core:base"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:model")) // jdk 11
}
