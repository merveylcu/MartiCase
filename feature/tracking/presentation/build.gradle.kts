import com.android.build.api.dsl.LibraryExtension

plugins {
    alias(libs.plugins.marticase.android.library.compose)
    alias(libs.plugins.marticase.android.hilt)
    alias(libs.plugins.marticase.android.robolectric)
    alias(libs.plugins.marticase.testing)
}

configure<LibraryExtension> {
    namespace = "com.merveylcu.marticase.feature.tracking.presentation"
}

dependencies {
    implementation(projects.core.designsystem)
    implementation(projects.core.permission)
    implementation(projects.feature.tracking.domain)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.bundles.compose)
    implementation(libs.bundles.lifecycle)
    implementation(libs.hilt.lifecycle.viewmodel.compose)
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.maps.compose)
    implementation(libs.maps.compose.utils)

    testImplementation(projects.core.testing)
    testImplementation(libs.androidx.compose.ui.test.junit4)
    testImplementation(libs.truth)

    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
