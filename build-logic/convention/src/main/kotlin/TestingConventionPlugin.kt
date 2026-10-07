import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class TestingConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val libs =
                extensions
                    .getByType(org.gradle.api.artifacts.VersionCatalogsExtension::class.java)
                    .named("libs")

            dependencies {
                "testImplementation"(libs.findLibrary("junit").get())
                "testImplementation"(libs.findLibrary("kotlinx-coroutines-test").get())
                "testImplementation"(libs.findLibrary("turbine").get())
                "testImplementation"(libs.findLibrary("mockk").get())
            }
        }
    }
}
