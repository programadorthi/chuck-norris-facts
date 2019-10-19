package configs

import java.io.File

class ProguardConfig(private val pathToFiles: String) {

    val customRules by lazy {
        File(pathToFiles).listFiles().toList().toTypedArray()
    }

    companion object {
        const val ANDROID_OPTIMIZE_FILE = "proguard-android-optimize.txt"
        // DON'T SAFE DELETE CONSTs BELOW
        // They are used in the .gradle files.
        const val CONSUMER_RULES = "consumer-rules.pro"
        const val PROGUARD_RULES = "proguard-rules.pro"
    }

}
