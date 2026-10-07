import org.gradle.api.JavaVersion

object ProjectConfig {
    const val COMPILE_SDK = 37
    const val MIN_SDK = 24
    const val TARGET_SDK = 37
    const val JVM_TOOLCHAIN = 17

    val JAVA_VERSION: JavaVersion = JavaVersion.VERSION_17

    const val TEST_INSTRUMENTATION_RUNNER = "androidx.test.runner.AndroidJUnitRunner"
}
