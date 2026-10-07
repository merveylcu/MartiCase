import com.android.build.api.dsl.LibraryExtension

plugins {
    alias(libs.plugins.marticase.android.library.compose)
    alias(libs.plugins.marticase.android.hilt)
    alias(libs.plugins.marticase.testing)
}

configure<LibraryExtension> {
    namespace = "com.merveylcu.marticase.feature.tracking.presentation"
}

dependencies {
    implementation(projects.feature.tracking.domain)
    implementation(projects.core.designsystem)
    implementation(projects.core.permission)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.bundles.compose)
    implementation(libs.androidx.compose.material.icons)
    implementation(libs.bundles.lifecycle)
    implementation(libs.hilt.lifecycle.viewmodel.compose)
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.maps.compose)

    testImplementation(projects.core.testing)
    testImplementation(libs.truth)
}
