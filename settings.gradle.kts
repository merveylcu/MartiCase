pluginManagement {
    includeBuild("build-logic")
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

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "MartiCase"
include(":app")
include(":core:common")
include(":core:database")
include(":core:datastore")
include(":core:designsystem")
include(":core:permission")
include(":core:testing")
include(":feature:tracking:domain")
include(":feature:tracking:data")
include(":feature:tracking:presentation")
