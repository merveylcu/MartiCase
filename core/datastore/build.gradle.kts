import com.android.build.api.dsl.LibraryExtension

plugins {
    alias(libs.plugins.marticase.android.library)
    alias(libs.plugins.marticase.android.hilt)
}

configure<LibraryExtension> {
    namespace = "com.merveylcu.marticase.core.datastore"
}

dependencies {
    implementation(projects.core.common)

    api(libs.androidx.datastore.preferences)
    implementation(libs.kotlinx.coroutines.core)
}
