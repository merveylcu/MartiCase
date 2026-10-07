import dev.detekt.gradle.extensions.DetektExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureDetekt() {
    pluginManager.apply("dev.detekt")

    extensions.configure<DetektExtension> {
        config.setFrom(rootProject.files("config/detekt/detekt.yml"))
        buildUponDefaultConfig.set(true)
        autoCorrect.set(true)
        baseline.set(project.layout.projectDirectory.file("detekt-baseline.xml"))
    }

    dependencies {
        "detektPlugins"(
            libs.findLibrary("detekt-compose-rules").get(),
        )
    }
}

private val Project.libs
    get() =
        extensions
            .getByType(org.gradle.api.artifacts.VersionCatalogsExtension::class.java)
            .named("libs")
