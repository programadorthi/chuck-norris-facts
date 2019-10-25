package br.com.programadorthi.facts.di

import br.com.programadorthi.facts.facts.FactsActivity
import br.com.programadorthi.facts.facts.FactsViewModel
import br.com.programadorthi.facts.search.SearchFactsActivity
import br.com.programadorthi.facts.search.SearchFactsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val factsUiModule = module {
    scope(named<FactsActivity>()) {
        viewModel { FactsViewModel(factsUseCase = get()) }
    }

    scope(named<SearchFactsActivity>()) {
        viewModel { SearchFactsViewModel(factsUseCase = get()) }
    }
}
