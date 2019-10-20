package br.com.programadorthi.facts

import br.com.programadorthi.facts.fake.FactsRepositoryFake
import org.junit.Before
import org.junit.Test
import kotlin.random.Random

class FactsUseCaseTest {

    private val random = Random.Default

    private lateinit var factsRepositoryFake: FactsRepositoryFake

    private lateinit var factsUseCase: FactsUseCase

    @Before
    fun `before each test`() {
        factsRepositoryFake = FactsRepositoryFake()

        factsUseCase = FactsUseCaseImpl(factsRepositoryFake)
    }

    @Test
    fun `should get empty categories when there is no categories in the data layer`() {
        val offset = random.nextInt(1, 8)

        val testObserver = factsUseCase.categories(offset).test()

        testObserver
            .assertNoErrors()
            .assertComplete()
            .assertValue { it.isEmpty() }
    }

    @Test
    fun `should get categories when there is categories in the data layer`() {
        val categories = listOf("cat1", "cat2", "cat3", "cat4", "cat5")

        val offset = random.nextInt(1, categories.size + 1)

        val expected = categories.slice(IntRange(start = 0, endInclusive = offset - 1))

        factsRepositoryFake.categories = categories

        val testObserver = factsUseCase.categories(offset).test()

        testObserver
            .assertNoErrors()
            .assertComplete()
            .assertValue(expected)
    }

    @Test
    fun `should search result is empty when the term has no results`() {
        val testObserver = factsUseCase.search("something").test()

        testObserver
            .assertNoErrors()
            .assertComplete()
            .assertValue { it.isEmpty() }
    }

    @Test
    fun `should search result is not empty when the term has results`() {
        val term = "something"
        val results = listOf(Fact(id = "1", url = "url", value = "value", categories = emptyList()))

        factsRepositoryFake.termsAndResults = mapOf(term to results)

        val testObserver = factsUseCase.search(term).test()

        testObserver
            .assertNoErrors()
            .assertComplete()
            .assertValue(results)
    }

    @Test
    fun `should has EmptySearch when has no term to search`() {
        val testObserver = factsUseCase.search("").test()

        testObserver
            .assertNoValues()
            .assertNotComplete()
            .assertError(FactsBusiness.EmptySearch)
    }
}
