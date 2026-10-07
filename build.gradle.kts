plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt.android) apply false
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.module.graph.assert)
    alias(libs.plugins.spotless)
}

spotless {
    kotlin {
        target("**/*.kt")
        targetExclude("**/build/**/*.kt")
        ktlint(libs.versions.ktlint.get()).setEditorConfigPath(rootProject.file(".editorconfig"))
    }
    kotlinGradle {
        target("**/*.kts")
        targetExclude("**/build/**/*.kts")
        ktlint(libs.versions.ktlint.get()).setEditorConfigPath(rootProject.file(".editorconfig"))
    }
}

moduleGraphAssert {
    maxHeight = 4
    restricted =
        arrayOf(
            ":core:.* -X> :feature:.*",
            ":feature:.*:domain -X> :feature:.*:data",
            ":feature:.*:domain -X> :feature:.*:presentation",
            ":feature:.*:presentation -X> :feature:.*:data",
        )
}
