package br.com.programadorthi.facts.domain

import br.com.programadorthi.domain.ResultTypes
import br.com.programadorthi.facts.fakes.FactsRepositoryFake
import kotlin.random.Random
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

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
    fun `should get empty categories when there is no categories in the data layer`() =
        runBlockingTest {
            val expected = ResultTypes.Success(emptyList<String>())
            val limit = random.nextInt(8)
            val categories = factsUseCase.categories(limit, true)
            assertThat(categories).isEqualTo(expected)
        }

    @Test
    fun `should get categories when there is categories in the data layer`() = runBlockingTest {
        val categories = listOf("cat1", "cat2", "cat3", "cat4", "cat5")
        val limit = random.nextInt(categories.size)
        val expected = categories.slice(IntRange(start = 0, endInclusive = limit - 1))
        factsRepositoryFake.categories = categories
        val result = factsUseCase.categories(limit, false)
        assertThat(result).isEqualTo(ResultTypes.Success(expected))
    }

    @Test
    fun `should get shuffled categories when shuffle is true`() = runBlockingTest {
        val categories = listOf("cat1", "cat2", "cat3", "cat4", "cat5").shuffled()
        val limit = random.nextInt(categories.size)
        val expected = categories.slice(IntRange(start = 0, endInclusive = limit - 1))
        factsRepositoryFake.categories = categories
        val result = factsUseCase.categories(limit, true)
        assertThat(result).isEqualTo(ResultTypes.Success(expected))
    }

    @Test
    fun `should search result is empty when the term has no results`() = runBlockingTest {
        val expected = ResultTypes.Success(emptyList<String>())
        val result = factsUseCase.search("something")
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `should search result is not empty when the term has results`() = runBlockingTest {
        val term = "something"
        val expected = listOf(
            Fact(id = "1", url = "url", value = "value", categories = emptyList())
        )
        factsRepositoryFake.termsAndResults = mapOf(term to expected)
        val result = factsUseCase.search(term)
        assertThat(result).isEqualTo(ResultTypes.Success(expected))
    }

    @Test
    fun `should has EmptySearch when has no term to search`() = runBlockingTest {
        val expected = FactsBusiness.EmptySearch
        val result = factsUseCase.search("")
        assertThat(result).isEqualTo(expected)
    }
}
