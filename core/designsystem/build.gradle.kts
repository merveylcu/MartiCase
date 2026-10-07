import com.android.build.api.dsl.LibraryExtension

plugins {
    alias(libs.plugins.marticase.android.library.compose)
}

configure<LibraryExtension> {
    namespace = "com.merveylcu.marticase.core.designsystem"
}

dependencies {
    implementation(libs.bundles.compose)
    implementation(libs.androidx.compose.material.icons)
}
