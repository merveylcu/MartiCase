import com.android.build.api.dsl.LibraryExtension

plugins {
    alias(libs.plugins.marticase.android.library)
    alias(libs.plugins.marticase.android.hilt)
    alias(libs.plugins.marticase.android.robolectric)
    alias(libs.plugins.marticase.testing)
}

configure<LibraryExtension> {
    namespace = "com.merveylcu.marticase.core.database"
}

dependencies {
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    testImplementation(libs.truth)
}
