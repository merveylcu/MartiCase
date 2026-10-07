import com.android.build.api.dsl.LibraryExtension

plugins {
    alias(libs.plugins.marticase.android.library)
    alias(libs.plugins.marticase.android.hilt)
}

configure<LibraryExtension> {
    namespace = "com.merveylcu.marticase.core.common"
}

dependencies {
    api(libs.kotlinx.coroutines.core)
}
