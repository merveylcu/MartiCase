import com.android.build.api.dsl.ApplicationExtension
import java.util.Properties

plugins {
    alias(libs.plugins.marticase.android.application)
    alias(libs.plugins.marticase.android.hilt)
    alias(libs.plugins.marticase.android.versioning)
}

val mapsApiKey: String =
    Properties()
        .apply {
            rootProject.file("local.properties").takeIf { it.exists() }?.inputStream()?.use(::load)
        }.getProperty("MAPS_API_KEY")
        ?: providers.environmentVariable("MAPS_API_KEY").orNull
        ?: ""

configure<ApplicationExtension> {
    namespace = "com.merveylcu.marticase"

    defaultConfig {
        applicationId = "com.merveylcu.marticase"
        manifestPlaceholders["mapsApiKey"] = mapsApiKey
    }

    buildTypes {
        debug {
            isDebuggable = true
            isMinifyEnabled = false
            applicationIdSuffix = ".debug"
        }
        release {
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"))
        }
    }
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.designsystem)
    implementation(projects.feature.tracking.data)
    implementation(projects.feature.tracking.domain)
    implementation(projects.feature.tracking.presentation)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.bundles.compose)
    implementation(libs.bundles.lifecycle)
}
