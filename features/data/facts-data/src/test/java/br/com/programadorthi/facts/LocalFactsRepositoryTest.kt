package br.com.programadorthi.facts

import br.com.programadorthi.facts.fake.PreferencesManagerFake
import br.com.programadorthi.facts.local.LocalFactsRepository
import br.com.programadorthi.facts.local.LocalFactsRepositoryImpl
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class LocalFactsRepositoryTest {

    private val lastSearches = "[\"animal\",\"weather\",\"career\",\"fashion\",\"culture\"]"

    private lateinit var preferencesManager: PreferencesManagerFake

    private lateinit var localFactsRepository: LocalFactsRepository

    @Before
    fun `before each test`() {
        preferencesManager = PreferencesManagerFake()

        localFactsRepository = LocalFactsRepositoryImpl(preferencesManager)
    }

    @Test
    fun `should have no search when last searches is empty`() {
        val testObserver = localFactsRepository.getLastSearches().test()

        testObserver
            .assertComplete()
            .assertNoErrors()
            .assertValue(emptyList())
    }

    @Test
    fun `should have searches when last searches is not empty`() {
        preferencesManager.putItem(LAST_SEARCHES_KEY, lastSearches)

        val expected = jsonParse.decodeFromString(ListSerializer(String.serializer()), lastSearches).asReversed()

        val testObserver = localFactsRepository.getLastSearches().test()

        testObserver
            .assertComplete()
            .assertNoErrors()
            .assertValue(expected)
            .assertOf { assertThat(preferencesManager.lastSearches).isEqualTo(lastSearches) }
    }

    @Test
    fun `should have ordered searches when put a new term in the history`() {
        val searchesList = jsonParse.decodeFromString(ListSerializer(String.serializer()), lastSearches)
        val searchesWithNewTerm = searchesList + "act"
        val searchesString = jsonParse.encodeToString(ListSerializer(String.serializer()), searchesWithNewTerm)

        preferencesManager.putItem(LAST_SEARCHES_KEY, searchesString)

        val expected = searchesWithNewTerm.asReversed()

        val testObserver = localFactsRepository.getLastSearches().test()

        testObserver
            .assertComplete()
            .assertNoErrors()
            .assertValue(expected)
            .assertOf { assertThat(preferencesManager.lastSearches).isEqualTo(searchesString) }
    }

    @Test
    fun `should keep the same searches list when search for a previous term`() {
        preferencesManager.lastSearches = lastSearches

        localFactsRepository.saveNewSearch("animal")

        assertThat(preferencesManager.lastSearches).isEqualTo(lastSearches)
    }

    @Test
    fun `should increase searches list when search for a new term`() {
        val searchesList = jsonParse.decodeFromString(ListSerializer(String.serializer()), lastSearches)
        val searchesWithNewTerm = searchesList + "act"
        val searchesString = jsonParse.encodeToString(ListSerializer(String.serializer()), searchesWithNewTerm)

        preferencesManager.lastSearches = lastSearches

        localFactsRepository.saveNewSearch("act")

        assertThat(preferencesManager.lastSearches).isEqualTo(searchesString)
    }

    private companion object {
        private const val LAST_SEARCHES_KEY = "last_searches"

        private val jsonParse = Json
    }
}