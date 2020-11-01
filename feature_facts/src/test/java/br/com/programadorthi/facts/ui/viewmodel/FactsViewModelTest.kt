package br.com.programadorthi.facts.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.programadorthi.domain.Result
import br.com.programadorthi.facts.Fact
import br.com.programadorthi.facts.ui.facts.FactsViewModel
import br.com.programadorthi.facts.fake.FactsUseCaseFake
import br.com.programadorthi.facts.ui.model.FactViewData
import io.reactivex.schedulers.Schedulers
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FactsViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var factsUseCase: FactsUseCaseFake

    private lateinit var factsViewModel: br.com.programadorthi.facts.ui.facts.FactsViewModel

    @Before
    fun `before each test`() {
        factsUseCase = FactsUseCaseFake()

        factsViewModel = br.com.programadorthi.facts.ui.facts.FactsViewModel(
            Schedulers.trampoline(),
            factsUseCase
        )
    }

    @Test
    fun `should have null facts when start app`() {
        val expected = null

        assertThat(factsViewModel.facts.value).isEqualTo(expected)
    }

    @Test
    fun `should have empty facts when search have no results`() {
        val expected = Result.Success(emptyList<br.com.programadorthi.facts.ui.model.FactViewData>())

        factsViewModel.search("")

        assertThat(factsViewModel.facts.value).isEqualTo(expected)
    }

    @Test
    fun `should have facts when search have results`() {
        val fact = Fact(
            id = "1",
            url = "url",
            value = "value",
            categories = emptyList()
        )

        val factViewData = br.com.programadorthi.facts.ui.model.FactViewData(
            category = "",
            url = fact.url,
            value = fact.value
        )

        val expected = Result.Success(listOf(factViewData))

        factsUseCase.searchResult = listOf(fact)

        factsViewModel.search("")

        assertThat(factsViewModel.facts.value).isEqualTo(expected)
    }

    @Test
    fun `should have error when search throw any exception`() {
        val exception = Exception("some operation")

        val expected = Result.Error(exception)

        factsUseCase.searchException = exception

        factsViewModel.search("")

        assertThat(factsViewModel.facts.value).isEqualTo(expected)
    }

    @Test
    fun `should have a loading and error flow when search throw any exception`() {
        val results = mutableListOf<Result<List<br.com.programadorthi.facts.ui.model.FactViewData>>>()

        val exception = Exception("some operation")

        val expected = listOf(Result.Loading, Result.Error(exception))

        factsUseCase.searchException = exception

        factsViewModel.facts.observeForever {
            results.add(it)
        }

        factsViewModel.search("")

        assertThat(results).isEqualTo(expected)
    }

    @Test
    fun `should have a loading and success flow when search have results`() {
        val fact = Fact(
            id = "1",
            url = "url",
            value = "value",
            categories = emptyList()
        )

        val factViewData = br.com.programadorthi.facts.ui.model.FactViewData(
            category = "",
            url = fact.url,
            value = fact.value
        )

        val results = mutableListOf<Result<List<br.com.programadorthi.facts.ui.model.FactViewData>>>()

        val expected = listOf(Result.Loading, Result.Success(listOf(factViewData)))

        factsUseCase.searchResult = listOf(fact)

        factsViewModel.facts.observeForever {
            results.add(it)
        }

        factsViewModel.search("")

        assertThat(results).isEqualTo(expected)
    }
}