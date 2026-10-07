plugins {
    alias(libs.plugins.marticase.kotlin.library)
    alias(libs.plugins.marticase.kotlin.lint)
}

dependencies {
    api(libs.kotlinx.coroutines.core)
    implementation(libs.javax.inject)
}
