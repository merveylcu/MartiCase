import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import java.util.Properties

class AndroidVersioningConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.withPlugin("com.android.application") {
                extensions.configure<ApplicationExtension> {
                    val versionProps =
                        Properties().also { props ->
                            rootProject.file("version.properties").inputStream().use(props::load)
                        }
                    defaultConfig.versionCode =
                        versionProps.getProperty("VERSION_CODE").trim().toInt()
                    defaultConfig.versionName = versionProps.getProperty("VERSION_NAME").trim()

                    val gitSha =
                        runCatching {
                            providers
                                .exec {
                                    commandLine("git", "rev-parse", "--short", "HEAD")
                                }.standardOutput.asText
                                .get()
                                .trim()
                        }.getOrNull()?.ifBlank { null }

                    val gitTag =
                        runCatching {
                            providers
                                .exec {
                                    commandLine("git", "describe", "--tags", "--always")
                                }.standardOutput.asText
                                .get()
                                .trim()
                        }.getOrNull()?.ifBlank { null }

                    val releaseSuffix =
                        System
                            .getenv("RELEASE_VERSION_SUFFIX")
                            ?.takeIf { it.isNotBlank() }
                            ?.let { "-$it" }
                            ?: gitTag?.let { "-$it" }.orEmpty()

                    val debugSuffix = gitSha?.let { "-debug-$it" } ?: "-debug"

                    buildTypes {
                        getByName("debug") {
                            versionNameSuffix = debugSuffix
                        }
                        getByName("release") {
                            versionNameSuffix = releaseSuffix
                        }
                    }
                }
            }
        }
    }
}
