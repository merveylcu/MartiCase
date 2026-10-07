import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project

internal fun Project.configureLint(commonExtension: CommonExtension) {
    commonExtension.lint.apply {
        abortOnError = true
        warningsAsErrors = true
        checkDependencies = true
        lintConfig = rootProject.file("config/lint/lint.xml")
        val baselineFile = project.file("lint-baseline.xml")
        if (baselineFile.exists()) {
            baseline = baselineFile
        }
    }
}
