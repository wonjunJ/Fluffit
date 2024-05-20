@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    `kotlin-dsl`
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

group = "com.kiwa.fluffit.buildlogic"

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
}

gradlePlugin{
    plugins{
        register("AndroidApplicationConventionPlugin"){
            id = "fluffit.plugin.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("AndroidLibraryConventionPlugin"){
            id = "fluffit.plugin.androidlibrary"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("AndroidLibraryComposeConventionPlugin"){
            id = "fluffit.plugin.compose"
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }
        register("JavaLibraryConventionPlugin"){
            id = "fluffit.plugin.javalibrary"
            implementationClass = "JavaLibraryConventionPlugin"
        }
        register("FeatureConventionPlugin"){
            id = "fluffit.plugin.feature"
            implementationClass = "FeatureConventionPlugin"
        }
        register("AndroidHiltConventionPlugin"){
            id = "fluffit.plugin.hilt"
            implementationClass = "AndroidHiltConventionPlugin"
        }
    }
}
