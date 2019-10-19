package configs

object FlavorConfig {
    const val DEFAULT_DIMENSION_NAME = "default"

    object BuildType {
        const val DEBUG = "debug"
        const val RELEASE = "release"
    }

    object Endpoint {
        const val DEVELOPMENT = "\"https://api.chucknorris.io/jokes/\""
        const val PRODUCTION = "\"https://api.chucknorris.io/jokes/\""
    }

    object Flavor {
        const val DEVELOPMENT = "dev"
        const val PRODUCTION = "prod"
    }
}
