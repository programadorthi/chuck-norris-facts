package br.com.programadorthi.facts.di

import br.com.programadorthi.facts.Fact
import br.com.programadorthi.facts.FactsRepository
import br.com.programadorthi.facts.FactsRepositoryImpl
import br.com.programadorthi.facts.local.LocalFactsRepository
import br.com.programadorthi.facts.local.LocalFactsRepositoryImpl
import br.com.programadorthi.facts.remote.FactsService
import br.com.programadorthi.facts.remote.mapper.FactsMapper
import br.com.programadorthi.facts.remote.raw.FactsResponseRaw
import br.com.programadorthi.facts.remote.repository.RemoteFactsRepository
import br.com.programadorthi.facts.remote.repository.RemoteFactsRepositoryImpl
import br.com.programadorthi.network.mapper.RemoteMapper
import org.koin.dsl.module
import retrofit2.Retrofit

val factsDataModule = module {
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
}
