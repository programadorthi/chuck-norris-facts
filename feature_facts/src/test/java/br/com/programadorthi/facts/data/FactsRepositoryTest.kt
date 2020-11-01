package br.com.programadorthi.facts.data

import br.com.programadorthi.facts.fake.LocalFactsRepositoryFake
import br.com.programadorthi.facts.fake.RemoteFactsRepositoryFake
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatIllegalArgumentException
import org.junit.Before
import org.junit.Test
import java.io.IOException
import kotlin.random.Random

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
    fun `should get empty categories when there is no local and remote data`() {
        val limit = random.nextInt(categories.size)

        val testObserver = factsRepository.fetchCategories(limit, false).test()

        testObserver
            .assertNoErrors()
            .assertComplete()
            .assertValue(emptyList())
    }

    @Test
    fun `should get empty categories when limit is zero`() {
        val limit = 0

        val testObserver = factsRepository.fetchCategories(limit, false).test()

        testObserver
            .assertNoErrors()
            .assertComplete()
            .assertValue(emptyList())
    }

    @Test
    fun `should get empty facts when not found facts for searchable term`() {
        val testObserver = factsRepository.doSearch("term").test()

        testObserver
            .assertNoErrors()
            .assertComplete()
            .assertValue(emptyList())
    }

    @Test
    fun `should get empty last searches when there is no history search`() {
        val testObserver = factsRepository.getLastSearches().test()

        testObserver
            .assertNoErrors()
            .assertComplete()
            .assertValue(emptyList())
    }

    @Test
    fun `should get not empty categories when there is local data`() {
        val limit = random.nextInt(1, categories.size)
        val expected = categories.slice(IntRange(0, limit - 1))

        localFactsRepositoryFake.categories = expected

        val testObserver = factsRepository.fetchCategories(limit, false).test()

        testObserver
            .assertNoErrors()
            .assertComplete()
            .assertValue(expected)
    }

    @Test
    fun `should get not empty categories when there is remote data`() {
        val limit = random.nextInt(1, categories.size)
        val expected = categories.slice(IntRange(0, limit - 1))

        remoteFactsRepositoryFake.categories = expected

        val testObserver = factsRepository.fetchCategories(limit, false).test()

        testObserver
            .assertNoErrors()
            .assertComplete()
            .assertValue(expected)
            .assertOf { assertThat(localFactsRepositoryFake.categories).isEqualTo(expected) }
    }

    @Test
    fun `should get error when fetching categories`() {
        val limit = random.nextInt(categories.size)
        val expected = IOException("categories error")

        remoteFactsRepositoryFake.fetchCategoriesError = expected

        val testObserver = factsRepository.fetchCategories(limit, false).test()

        testObserver
            .assertNotComplete()
            .assertNoValues()
            .assertError(expected)
    }

    @Test
    fun `should get last searches when there is history terms`() {
        val expected = listOf("term1", "term2")

        localFactsRepositoryFake.saveNewSearch(expected[0])
        localFactsRepositoryFake.saveNewSearch(expected[1])

        val testObserver = factsRepository.getLastSearches().test()

        testObserver
            .assertNoErrors()
            .assertComplete()
            .assertValue(expected)
    }

    @Test
    fun `should get searches result when searchable term has data`() {
        val expected = listOf(Fact("id", "url", "value", emptyList()))
        val expectedTerm = "term1"

        remoteFactsRepositoryFake.facts = expected

        val testObserver = factsRepository.doSearch(expectedTerm).test()

        testObserver
            .assertNoErrors()
            .assertComplete()
            .assertValue(expected)
            .assertOf {
                assertThat(localFactsRepositoryFake.lastSearches).isEqualTo(listOf(expectedTerm))
            }
    }

    @Test
    fun `should get searches error when searchable term throw exception`() {
        val expected = IOException("search error")
        val expectedTerm = "term1"

        remoteFactsRepositoryFake.searchError = expected

        val testObserver = factsRepository.doSearch(expectedTerm).test()

        testObserver
            .assertNotComplete()
            .assertNoValues()
            .assertError(expected)
            .assertOf {
                assertThat(localFactsRepositoryFake.lastSearches).isEqualTo(emptyList<String>())
            }
    }

    @Test
    fun `should get empty shuffled categories when shuffle is true and limit is zero`() {
        val limit = 0

        remoteFactsRepositoryFake.categories = categories

        val testObserver = factsRepository.fetchCategories(limit, true).test()

        testObserver
            .assertNoErrors()
            .assertComplete()
            .assertValue(emptyList())
            .assertOf {
                assertThat(localFactsRepositoryFake.categories).isEqualTo(emptyList<String>())
            }
    }

    @Test
    fun `should be impossible shuffle categories when have one category only`() {
        val limit = random.nextInt(1, categories.size)
        val expected = categories.slice(IntRange(0, 0))

        remoteFactsRepositoryFake.categories = expected

        val testObserver = factsRepository.fetchCategories(limit, true).test()

        testObserver
            .assertNoErrors()
            .assertComplete()
            .assertValue(expected)
            .assertOf { assertThat(localFactsRepositoryFake.categories).isEqualTo(expected) }
    }

    @Test
    fun `should get shuffled categories when shuffle is true and have more the one category`() {
        val limit = random.nextInt(2, categories.size)
        val expected = categories.slice(IntRange(0, limit - 1))

        remoteFactsRepositoryFake.categories = expected

        val testObserver = factsRepository.fetchCategories(limit, true).test()

        testObserver
            .assertNoErrors()
            .assertComplete()
            .assertValue { value -> value != expected }
            .assertOf { assertThat(localFactsRepositoryFake.categories).isEqualTo(expected) }
    }

    @Test
    fun `should get an IllegalArgumentException when limit is less than zero`() {
        val limit = -1

        assertThatIllegalArgumentException().isThrownBy {
            factsRepository.fetchCategories(limit, false).test()
        }.withMessage("count >= 0 required but it was -1")
    }
}
