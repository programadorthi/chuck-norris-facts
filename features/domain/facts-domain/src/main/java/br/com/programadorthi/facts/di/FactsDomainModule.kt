package br.com.programadorthi.facts.di

import br.com.programadorthi.facts.FactsUseCase
import br.com.programadorthi.facts.FactsUseCaseImpl
import org.koin.dsl.module

val factsDomainModule = module {
    factory<FactsUseCase> { FactsUseCaseImpl(factsRepository = get()) }
}
