package br.com.programadorthi.facts.ui.viewmodel

import br.com.programadorthi.chucknorrisfacts.ui.UIState
import br.com.programadorthi.facts.domain.Fact
import br.com.programadorthi.facts.fakes.FactsUseCaseFake
import br.com.programadorthi.facts.fakes.StringProviderFake
import br.com.programadorthi.facts.ui.model.FactViewData
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import com.google.android.material.R as materialR

class FactsViewModelTest {

    private val testScope = TestCoroutineScope()
    private lateinit var factsUseCase: FactsUseCaseFake
    private lateinit var stringProvider: StringProviderFake
    private lateinit var factsViewModel: FactsViewModel

    @Before
    fun `before each test`() {
        factsUseCase = FactsUseCaseFake()
        stringProvider = StringProviderFake("123 default")
        factsViewModel = FactsViewModel(factsUseCase, stringProvider, testScope)
    }

    @After
    fun `after each test`() {
        testScope.cleanupTestCoroutines()
    }

    @Test
    fun `should have null facts when start app`() = testScope.runBlockingTest {
        val expected = UIState.Idle
        assertThat(factsViewModel.facts.value).isEqualTo(expected)
    }

    @Test
    fun `should have empty facts when search have no results`() = testScope.runBlockingTest {
        val expected = UIState.Success(emptyList<FactViewData>())
        factsViewModel.search("")
        assertThat(factsViewModel.facts.value).isEqualTo(expected)
    }

    @Test
    fun `should have facts when search have results`() = testScope.runBlockingTest {
        val fact = Fact(
            id = "1",
            url = "url",
            value = "value",
            categories = emptyList()
        )

        val factViewData = FactViewData(
            category = stringProvider.textToReturn,
            url = fact.url,
            value = fact.value,
            style = materialR.style.TextAppearance_MaterialComponents_Headline4
        )

        val expected = UIState.Success(listOf(factViewData))
        factsUseCase.searchResult = listOf(fact)
        factsViewModel.search("")
        assertThat(factsViewModel.facts.value).isEqualTo(expected)
    }

    @Test
    fun `should have error when search throw any exception`() = testScope.runBlockingTest {
        val exception = Exception("some operation")
        stringProvider.textToReturn = "this is the message"
        val expected = UIState.Error(exception, stringProvider.textToReturn)
        factsUseCase.searchException = exception
        factsViewModel.search("")
        assertThat(factsViewModel.facts.value).isEqualTo(expected)
    }

    @Test
    fun `should have a loading and error flow when search throw any exception`() =
        testScope.runBlockingTest {
            val exception = Exception("some operation")
            stringProvider.textToReturn = "this is the message"
            val expected = listOf(
                UIState.Idle,
                UIState.Loading,
                UIState.Error(exception, stringProvider.textToReturn)
            )
            factsUseCase.searchException = exception

            val results = mutableListOf<UIState<List<FactViewData>>>()
            val job = launch {
                factsViewModel.facts.toList(results)
            }
            factsViewModel.search("")
            job.cancelAndJoin()

            assertThat(results).isEqualTo(expected)
        }

    @Test
    fun `should have a loading and success flow when search have results`() =
        testScope.runBlockingTest {
            val fact = Fact(
                id = "1",
                url = "url",
                value = "value maior que 80 value maior que 80 value maior que 80 value value " +
                        "maior que 80 value maior que 80 value maior que 80 value",
                categories = emptyList()
            )

            val factViewData = FactViewData(
                category = stringProvider.textToReturn,
                url = fact.url,
                value = fact.value,
                style = materialR.style.TextAppearance_MaterialComponents_Subtitle1
            )

            factsUseCase.searchResult = listOf(fact)

            val expected =
                listOf(UIState.Idle, UIState.Loading, UIState.Success(listOf(factViewData)))
            val results = mutableListOf<UIState<List<FactViewData>>>()
            val job = launch {
                factsViewModel.facts.toList(results)
            }
            factsViewModel.search("")
            job.cancelAndJoin()

            assertThat(results).isEqualTo(expected)
        }
}