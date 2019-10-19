package configs

object FlavorConfig {
    const val DEFAULT_DIMENSION_NAME = "default"

    object Endpoint {
        const val DEVELOPMENT = "\"https://api.chucknorris.io/jokes/\""
        const val PRODUCTION = "\"https://api.chucknorris.io/jokes/\""
    }

    object Flavor {
        const val DEVELOPMENT = "dev"
        const val PRODUCTION = "prod"
    }
}
