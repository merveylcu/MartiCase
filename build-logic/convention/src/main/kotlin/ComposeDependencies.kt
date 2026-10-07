import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

internal fun Project.configureComposeDependencies() {
    val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
    val bom = libs.findLibrary("androidx-compose-bom").get()

    dependencies {
        "implementation"(platform(bom))
        "androidTestImplementation"(platform(bom))
        "debugImplementation"(libs.findLibrary("androidx-compose-ui-tooling").get())
    }
}
