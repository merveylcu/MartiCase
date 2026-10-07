plugins {
    alias(libs.plugins.marticase.kotlin.library)
    alias(libs.plugins.marticase.kotlin.lint)
}

dependencies {
    api(libs.junit)
    api(libs.kotlinx.coroutines.test)
}
