pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Fluffit"
include(":app")
include(":wear")
include(":feature:home")
include(":feature:mypage")
include(":feature:ranking")
include(":core:data")
include(":core:domain")
include(":feature:flupet-history")
include(":feature:battle-record")
include(":feature:collection")
include(":feature:login")
