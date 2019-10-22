package modules

object ProjectModules {

    const val APP = ":app"

    object Feature {
        object Data {
            private const val DATA_PREFIX = ":features:data"
            const val FACTS = "${DATA_PREFIX}:facts-data"
        }

        object Domain {
            private const val DOMAIN_PREFIX = ":features:domain"
            const val FACTS = "${DOMAIN_PREFIX}:facts-domain"
        }
    }

    object Shared {
        const val DOMAIN = ":shared-domain"
        const val NETWORK = ":shared-network"
    }

}
