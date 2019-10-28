package br.com.programadorthi.facts.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.programadorthi.domain.Result
import br.com.programadorthi.facts.fake.FactsUseCaseFake
import br.com.programadorthi.facts.search.SearchFactsViewModel
import io.reactivex.schedulers.Schedulers
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SearchFactsViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var factsUseCase: FactsUseCaseFake

    private lateinit var searchFactsViewModel: SearchFactsViewModel

    @Before
    fun `before each test`() {
        factsUseCase = FactsUseCaseFake()

        searchFactsViewModel = SearchFactsViewModel(Schedulers.trampoline(), factsUseCase)
    }

    @Test
    fun `should have null categories when start app`() {
        val expected = null

        assertThat(searchFactsViewModel.categories.value).isEqualTo(expected)
    }

    @Test
    fun `should have empty categories when server returns no categories`() {
        val expected = Result.Success(emptyList<String>())

        searchFactsViewModel.fetchCategories()

        assertThat(searchFactsViewModel.categories.value).isEqualTo(expected)
    }

    @Test
    fun `should have categories when have categories online or offline`() {
        val categories = listOf("fashion", "animal", "movies")

        val expected = Result.Success(categories)

        factsUseCase.categories = categories

        searchFactsViewModel.fetchCategories()

        assertThat(searchFactsViewModel.categories.value).isEqualTo(expected)
    }

    @Test
    fun `should have only eight categories when have more than eight categories available`() {
        val categories = listOf(
            "fashion",
            "animal",
            "movies",
            "cat1",
            "cat2",
            "cat3",
            "cat4",
            "cat5",
            "cat6",
            "cat7",
            "cat8"
        )

        val expected = Result.Success(categories.slice(IntRange(0, 7)))

        factsUseCase.categories = categories

        searchFactsViewModel.fetchCategories()

        assertThat(searchFactsViewModel.categories.value).isEqualTo(expected)
    }

    @Test
    fun `should have error when fetch categories`() {
        val exception = Exception("some operation")

        val expected = Result.Error(exception)

        factsUseCase.categoriesException = exception

        searchFactsViewModel.fetchCategories()

        assertThat(searchFactsViewModel.categories.value).isEqualTo(expected)
    }

    @Test
    fun `should have empty last searches when there never was a search`() {
        val expected = emptyList<String>()

        searchFactsViewModel.fetchLastSearches()

        assertThat(searchFactsViewModel.lastSearches.value).isEqualTo(expected)
    }

    @Test
    fun `should have a searches list when there is at least a search term`() {
        val expected = listOf("term")

        factsUseCase.lastSearches = expected

        searchFactsViewModel.fetchLastSearches()

        assertThat(searchFactsViewModel.lastSearches.value).isEqualTo(expected)
    }
}