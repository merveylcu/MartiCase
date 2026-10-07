import com.android.build.api.dsl.LibraryExtension

plugins {
    alias(libs.plugins.marticase.android.library.compose)
}

configure<LibraryExtension> {
    namespace = "com.merveylcu.marticase.core.permission"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.bundles.compose)
}
