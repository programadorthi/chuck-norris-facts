package br.com.programadorthi.network

import br.com.programadorthi.domain.InjectionTags
import br.com.programadorthi.network.exception.NetworkingErrorMapper
import br.com.programadorthi.network.exception.NetworkingErrorMapperImpl
import br.com.programadorthi.network.manager.DefaultNetworkManager
import br.com.programadorthi.network.manager.NetworkManager
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider
import org.kodein.di.singleton

val sharedNetworkModule = DI.Module("sharedNetworkModule") {
    bind<NetworkManager>() with singleton {
        DefaultNetworkManager(
            connectionCheck = instance(),
            networkingErrorMapper = instance(),
            ioDispatcher = instance(InjectionTags.IO_DISPATCHER)
        )
    }
    bind<NetworkingErrorMapper>() with provider {
        NetworkingErrorMapperImpl(
            crashReport = instance()
        )
    }
}
