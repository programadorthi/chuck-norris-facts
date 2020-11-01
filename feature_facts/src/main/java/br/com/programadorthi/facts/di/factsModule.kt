package br.com.programadorthi.facts.di

import br.com.programadorthi.domain.InjectionTags
import br.com.programadorthi.facts.data.FactsRepositoryImpl
import br.com.programadorthi.facts.data.local.LocalFactsRepository
import br.com.programadorthi.facts.data.local.LocalFactsRepositoryImpl
import br.com.programadorthi.facts.data.remote.FactsService
import br.com.programadorthi.facts.data.remote.mapper.FactsMapper
import br.com.programadorthi.facts.data.remote.raw.FactsResponseRaw
import br.com.programadorthi.facts.data.remote.repository.RemoteFactsRepository
import br.com.programadorthi.facts.data.remote.repository.RemoteFactsRepositoryImpl
import br.com.programadorthi.facts.domain.Fact
import br.com.programadorthi.facts.domain.FactsRepository
import br.com.programadorthi.facts.domain.FactsUseCase
import br.com.programadorthi.facts.domain.FactsUseCaseImpl
import br.com.programadorthi.facts.ui.viewmodel.FactsViewModel
import br.com.programadorthi.facts.ui.viewmodel.SearchFactsViewModel
import br.com.programadorthi.network.mapper.RemoteMapper
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider
import retrofit2.Retrofit

val factsModule = DI.Module("factsModule") {
    bind<FactsUseCase>() with provider { FactsUseCaseImpl(factsRepository = instance()) }

    bind<LocalFactsRepository>() with provider {
        LocalFactsRepositoryImpl(
            preferencesManager = instance(),
            ioDispatcher = instance(InjectionTags.IO_DISPATCHER)
        )
    }

    bind<RemoteMapper<FactsResponseRaw, List<Fact>>>() with provider { FactsMapper() }

    bind<RemoteFactsRepository>() with provider {
        RemoteFactsRepositoryImpl(
            factsMapper = instance(),
            factsService = instance(),
            networkManager = instance()
        )
    }

    bind<FactsService>() with provider { instance<Retrofit>().create(FactsService::class.java) }

    bind<FactsRepository>() with provider {
        FactsRepositoryImpl(
            localFactsRepository = instance(),
            remoteFactsRepository = instance()
        )
    }

    bind<FactsViewModel>() with provider {
        FactsViewModel(
            factsUseCase = instance(),
            ioScope = instance(InjectionTags.IO_SCOPE)
        )
    }

    bind<SearchFactsViewModel>() with provider {
        SearchFactsViewModel(
            factsUseCase = instance(),
            ioScope = instance(InjectionTags.IO_SCOPE)
        )
    }
}