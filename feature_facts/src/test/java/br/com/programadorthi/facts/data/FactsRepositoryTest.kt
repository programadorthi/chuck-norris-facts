package br.com.programadorthi.facts.data

import br.com.programadorthi.domain.ResultTypes
import br.com.programadorthi.facts.domain.Fact
import br.com.programadorthi.facts.domain.FactsRepository
import br.com.programadorthi.facts.fakes.LocalFactsRepositoryFake
import br.com.programadorthi.facts.fakes.RemoteFactsRepositoryFake
import java.io.IOException
import kotlin.random.Random
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatIllegalArgumentException
import org.junit.Before
import org.junit.Test

class FactsRepositoryTest {

    private val categories = listOf("cat1", "cat2", "cat3", "cat4", "cat5", "cat6", "cat7", "cat8")

    private val random = Random.Default

    private lateinit var localFactsRepositoryFake: LocalFactsRepositoryFake

    private lateinit var remoteFactsRepositoryFake: RemoteFactsRepositoryFake

    private lateinit var factsRepository: FactsRepository

    @Before
    fun `before each test`() {
        localFactsRepositoryFake = LocalFactsRepositoryFake()

        remoteFactsRepositoryFake = RemoteFactsRepositoryFake()

        factsRepository = FactsRepositoryImpl(
            localFactsRepository = localFactsRepositoryFake,
            remoteFactsRepository = remoteFactsRepositoryFake
        )
    }

    @Test
    fun `should get empty categories when there is no local and remote data`() = runBlockingTest {
        val expected = ResultTypes.Success(emptyList<String>())
        val limit = random.nextInt(categories.size)
        val result = factsRepository.fetchCategories(limit, false)
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `should get empty categories when limit is zero`() = runBlockingTest {
        val limit = 0
        val expected = ResultTypes.Success(emptyList<String>())
        val result = factsRepository.fetchCategories(limit, false)
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `should get empty facts when not found facts for searchable term`() = runBlockingTest {
        val expected = ResultTypes.Success(emptyList<String>())
        val result = factsRepository.doSearch("term")
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `should get empty last searches when there is no history search`() = runBlockingTest {
        val expected = ResultTypes.Success(emptyList<String>())
        val result = factsRepository.getLastSearches()
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `should get not empty categories when there is local data`() = runBlockingTest {
        val limit = random.nextInt(1, categories.size)
        val expected = categories.slice(IntRange(0, limit - 1))
        localFactsRepositoryFake.categories = expected
        val result = factsRepository.fetchCategories(limit, false)
        assertThat(result).isEqualTo(ResultTypes.Success(expected))
    }

    @Test
    fun `should get not empty categories when there is remote data`() = runBlockingTest {
        val limit = random.nextInt(1, categories.size)
        val expected = categories.slice(IntRange(0, limit - 1))
        remoteFactsRepositoryFake.categories = expected
        val result = factsRepository.fetchCategories(limit, false)
        assertThat(result).isEqualTo(ResultTypes.Success(expected))
        assertThat(localFactsRepositoryFake.categories).isEqualTo(expected)
    }

    @Test
    fun `should get error when fetching categories`() = runBlockingTest {
        val limit = random.nextInt(categories.size)
        val exception = IOException("categories error")
        val expected = ResultTypes.Error(exception)
        remoteFactsRepositoryFake.fetchCategoriesError = exception
        val result = factsRepository.fetchCategories(limit, false)
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `should get last searches when there is history terms`() = runBlockingTest {
        val expected = listOf("term1", "term2")
        localFactsRepositoryFake.saveNewSearch(expected[0])
        localFactsRepositoryFake.saveNewSearch(expected[1])
        val result = factsRepository.getLastSearches()
        assertThat(result).isEqualTo(ResultTypes.Success(expected))
    }

    @Test
    fun `should get searches result when searchable term has data`() = runBlockingTest {
        val expected = listOf(Fact("id", "url", "value", emptyList()))
        val expectedTerm = "term1"
        remoteFactsRepositoryFake.facts = expected
        val result = factsRepository.doSearch(expectedTerm)
        assertThat(result).isEqualTo(ResultTypes.Success(expected))
        assertThat(localFactsRepositoryFake.lastSearches).isEqualTo(listOf(expectedTerm))
    }

    @Test
    fun `should get searches error when searchable term throw exception`() = runBlockingTest {
        val expectedTerm = "term1"
        val exception = IOException("search error")
        val expected = ResultTypes.Error(exception)
        remoteFactsRepositoryFake.searchError = exception
        val result = factsRepository.doSearch(expectedTerm)
        assertThat(result).isEqualTo(expected)
        assertThat(localFactsRepositoryFake.lastSearches).isEqualTo(emptyList<String>())
    }

    @Test
    fun `should get empty shuffled categories when shuffle is true and limit is zero`() =
        runBlockingTest {
            val limit = 0
            val expected = ResultTypes.Success(emptyList<String>())
            remoteFactsRepositoryFake.categories = categories
            val result = factsRepository.fetchCategories(limit, true)
            assertThat(result).isEqualTo(expected)
            assertThat(localFactsRepositoryFake.categories).isEqualTo(categories)
        }

    @Test
    fun `should be impossible shuffle categories when have one category only`() = runBlockingTest {
        val limit = random.nextInt(1, categories.size)
        val expected = categories.slice(IntRange(0, 0))
        remoteFactsRepositoryFake.categories = expected
        val result = factsRepository.fetchCategories(limit, true)
        assertThat(result).isEqualTo(ResultTypes.Success(expected))
        assertThat(localFactsRepositoryFake.categories).isEqualTo(expected)
    }

    @Test
    fun `should get shuffled categories when shuffle is true and have more the one category`() =
        runBlockingTest {
            val limit = random.nextInt(2, categories.size)
            val expected = categories.slice(IntRange(0, limit - 1))
            remoteFactsRepositoryFake.categories = expected
            val result = factsRepository.fetchCategories(limit, true)
            assertThat(result).isNotEqualTo(expected)
            assertThat(localFactsRepositoryFake.categories).isEqualTo(expected)
        }

    @Test
    fun `should get an IllegalArgumentException when limit is less than zero`() = runBlockingTest {
        val limit = -1
        assertThatIllegalArgumentException().isThrownBy {
            runBlocking {
                factsRepository.fetchCategories(limit, false)
            }
        }.withMessage("Requested element count -1 is less than zero.")
    }
}
