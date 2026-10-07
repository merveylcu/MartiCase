import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal fun Project.configureKotlinExplicitApi() {
    tasks
        .withType<KotlinCompile>()
        .matching { !it.name.contains("Test") }
        .configureEach {
            compilerOptions.freeCompilerArgs.add("-Xexplicit-api=strict")
        }
}
