plugins {
    `kotlin-dsl`
}

kotlin {
    jvmToolchain(
        libs.versions.jvmToolchain
            .get()
            .toInt(),
    )
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
    compileOnly(libs.hilt.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
    compileOnly(libs.detekt.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id =
                libs.plugins.marticase.android.application
                    .get()
                    .pluginId
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidLibrary") {
            id =
                libs.plugins.marticase.android.library
                    .asProvider()
                    .get()
                    .pluginId
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidLibraryCompose") {
            id =
                libs.plugins.marticase.android.library.compose
                    .get()
                    .pluginId
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }
        register("kotlinLibrary") {
            id =
                libs.plugins.marticase.kotlin.library
                    .get()
                    .pluginId
            implementationClass = "KotlinLibraryConventionPlugin"
        }
        register("androidHilt") {
            id =
                libs.plugins.marticase.android.hilt
                    .get()
                    .pluginId
            implementationClass = "HiltConventionPlugin"
        }
        register("androidVersioning") {
            id =
                libs.plugins.marticase.android.versioning
                    .get()
                    .pluginId
            implementationClass = "AndroidVersioningConventionPlugin"
        }
        register("kotlinLint") {
            id =
                libs.plugins.marticase.kotlin.lint
                    .get()
                    .pluginId
            implementationClass = "KotlinLintConventionPlugin"
        }
        register("androidRobolectric") {
            id =
                libs.plugins.marticase.android.robolectric
                    .get()
                    .pluginId
            implementationClass = "AndroidRobolectricConventionPlugin"
        }
        register("testing") {
            id =
                libs.plugins.marticase.testing
                    .get()
                    .pluginId
            implementationClass = "TestingConventionPlugin"
        }
    }
}
