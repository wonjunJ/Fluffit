plugins {
    alias(libs.plugins.fluffit.feature)
    alias(libs.plugins.ktlintLibrary)
    id("kotlin-kapt")
}

android {
    namespace = "com.kiwa.fluffit.login"

    configurations.implementation {
        exclude(group = "com.intellij", module = "annotations")
    }
}

dependencies {
    implementation(project(":feature:home"))
    implementation(project(":core:model"))

    implementation("com.navercorp.nid:oauth:5.9.1")
    implementation(project(":core:base"))
    implementation(project(":core:designsystem")) // jdk 11
}
