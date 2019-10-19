package configs

import org.gradle.api.Project
import java.io.File

object SigningConfig {
    const val KEY_ALIAS = "chucknorris"
    const val KEY_PASSWORD = "123456"
    const val STORE_PASSWORD = "123456"
    private const val STORE_FILE = "buildSrc/chucknorris.jks"

    @JvmStatic
    fun storeFile(rootProject: Project): File {
        return rootProject.file(STORE_FILE)
    }
}