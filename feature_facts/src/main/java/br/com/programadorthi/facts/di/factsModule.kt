package br.com.programadorthi.facts.di

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
import br.com.programadorthi.facts.ui.FactsActivity
import br.com.programadorthi.facts.ui.SearchFactsActivity
import br.com.programadorthi.facts.ui.viewmodel.FactsViewModel
import br.com.programadorthi.facts.ui.viewmodel.SearchFactsViewModel
import br.com.programadorthi.network.mapper.RemoteMapper
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

val factsModule = module {
    factory<FactsUseCase> { FactsUseCaseImpl(factsRepository = get()) }

    factory<LocalFactsRepository> { LocalFactsRepositoryImpl(preferencesManager = get()) }

    factory<RemoteMapper<FactsResponseRaw, List<Fact>>> { FactsMapper() }

    factory<RemoteFactsRepository> {
        RemoteFactsRepositoryImpl(
            factsMapper = get(),
            factsService = get(),
            networkManager = get()
        )
    }

    factory { get<Retrofit>().create(FactsService::class.java) }

    factory<FactsRepository> {
        FactsRepositoryImpl(
            localFactsRepository = get(),
            remoteFactsRepository = get()
        )
    }

    scope(named<FactsActivity>()) {
        viewModel {
            FactsViewModel(
                scheduler = get(),
                factsUseCase = get()
            )
        }
    }

    scope(named<SearchFactsActivity>()) {
        viewModel {
            SearchFactsViewModel(
                scheduler = get(),
                factsUseCase = get()
            )
        }
    }
}