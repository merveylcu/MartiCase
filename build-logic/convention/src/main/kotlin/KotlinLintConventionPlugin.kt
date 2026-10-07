import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class KotlinLintConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            tasks.withType<KotlinCompile>().configureEach {
                compilerOptions {
                    allWarningsAsErrors.set(true)
                }
            }
        }
    }
}
