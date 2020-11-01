package br.com.programadorthi.facts.ui.viewmodel

import br.com.programadorthi.facts.fakes.FactsUseCaseFake
import br.com.programadorthi.facts.ui.UIState
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test

class SearchFactsViewModelTest {

    private val testScope = TestCoroutineScope()
    private lateinit var factsUseCase: FactsUseCaseFake
    private lateinit var searchFactsViewModel: SearchFactsViewModel

    @Before
    fun `before each test`() {
        factsUseCase = FactsUseCaseFake()
        searchFactsViewModel = SearchFactsViewModel(factsUseCase, testScope)
    }

    @After
    fun `after each test`() {
        testScope.cleanupTestCoroutines()
    }

    @Test
    fun `should have null categories when start app`() = testScope.runBlockingTest {
        val expected = UIState.Idle
        assertThat(searchFactsViewModel.categories.value).isEqualTo(expected)
    }

    @Test
    fun `should have empty categories when server returns no categories`() =
        testScope.runBlockingTest {
            val expected = UIState.Success(emptyList<String>())
            searchFactsViewModel.fetchCategories()
            assertThat(searchFactsViewModel.categories.value).isEqualTo(expected)
        }

    @Test
    fun `should have categories when have categories online or offline`() =
        testScope.runBlockingTest {
            val categories = listOf("fashion", "animal", "movies")
            val expected = UIState.Success(categories)
            factsUseCase.categories = categories
            searchFactsViewModel.fetchCategories()
            assertThat(searchFactsViewModel.categories.value).isEqualTo(expected)
        }

    @Test
    fun `should have only eight categories when have more than eight categories available`() =
        testScope.runBlockingTest {
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

            val expected = UIState.Success(categories.slice(IntRange(0, 7)))
            factsUseCase.categories = categories
            searchFactsViewModel.fetchCategories()
            assertThat(searchFactsViewModel.categories.value).isEqualTo(expected)
        }

    @Test
    fun `should have error when fetch categories`() = testScope.runBlockingTest {
        val exception = Exception("some operation")
        val expected = UIState.Failed(exception)
        factsUseCase.categoriesException = exception
        searchFactsViewModel.fetchCategories()
        assertThat(searchFactsViewModel.categories.value).isEqualTo(expected)
    }

    @Test
    fun `should have empty last searches when there never was a search`() =
        testScope.runBlockingTest {
            val expected = UIState.Success(emptyList<String>())
            searchFactsViewModel.fetchLastSearches()
            assertThat(searchFactsViewModel.lastSearches.value).isEqualTo(expected)
        }

    @Test
    fun `should have a searches list when there is at least a search term`() =
        testScope.runBlockingTest {
            val list = listOf("term")
            val expected = UIState.Success(list)
            factsUseCase.lastSearches = list
            searchFactsViewModel.fetchLastSearches()
            assertThat(searchFactsViewModel.lastSearches.value).isEqualTo(expected)
        }
}