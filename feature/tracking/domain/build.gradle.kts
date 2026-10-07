plugins {
    alias(libs.plugins.marticase.kotlin.library)
    alias(libs.plugins.marticase.kotlin.lint)
    alias(libs.plugins.marticase.testing)
}

dependencies {
    implementation(libs.javax.inject)
    implementation(libs.kotlinx.coroutines.core)

    testImplementation(libs.truth)
}
