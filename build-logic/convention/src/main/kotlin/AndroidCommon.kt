import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project

internal fun Project.configureAndroidCommon(commonExtension: CommonExtension) {
    commonExtension.compileSdk = ProjectConfig.COMPILE_SDK
    commonExtension.defaultConfig.minSdk = ProjectConfig.MIN_SDK
    commonExtension.defaultConfig.testInstrumentationRunner =
        ProjectConfig.TEST_INSTRUMENTATION_RUNNER
    commonExtension.compileOptions.sourceCompatibility = ProjectConfig.JAVA_VERSION
    commonExtension.compileOptions.targetCompatibility = ProjectConfig.JAVA_VERSION

    configureDetekt()
    configureLint(commonExtension)
}
