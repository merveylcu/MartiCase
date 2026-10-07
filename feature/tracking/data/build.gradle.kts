import com.android.build.api.dsl.LibraryExtension

plugins {
    alias(libs.plugins.marticase.android.library)
    alias(libs.plugins.marticase.android.hilt)
    alias(libs.plugins.marticase.testing)
}

configure<LibraryExtension> {
    namespace = "com.merveylcu.marticase.feature.tracking.data"
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.database)
    implementation(projects.core.datastore)
    implementation(projects.feature.tracking.domain)

    implementation(libs.androidx.core.ktx)
    implementation(libs.play.services.location)
    implementation(libs.bundles.coroutines)

    testImplementation(libs.truth)
}
