package br.com.programadorthi.facts

import br.com.programadorthi.facts.remote.mapper.FactsMapper
import br.com.programadorthi.facts.remote.raw.FactRaw
import br.com.programadorthi.facts.remote.raw.FactsResponseRaw
import br.com.programadorthi.network.exception.NetworkingError.EssentialParamMissing
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.junit.Test

class FactsMapperTest {

    private val factsMapper = FactsMapper()

    @Test
    fun `should throw EssentialParamMissing when result field is null`() {
        val response = FactsResponseRaw()
        val missingParams = listOf(FactsResponseRaw.RESULT_FIELD)
        val expectedMessage = "The $response has missing parameters. They are: $missingParams"

        assertThatExceptionOfType(EssentialParamMissing::class.java)
            .isThrownBy { factsMapper.apply(response) }
            .withMessage(expectedMessage)
    }

    @Test
    fun `should throw EssentialParamMissing when items id field is null`() {
        val response = FactsResponseRaw(
            result = listOf(
                FactRaw(
                    id = null,
                    url = "",
                    value = ""
                )
            )
        )
        val missingParams = listOf(FactRaw.ID_FIELD)
        val expectedMessage = "The $response has missing parameters. They are: $missingParams"

        assertThatExceptionOfType(EssentialParamMissing::class.java)
            .isThrownBy { factsMapper.apply(response) }
            .withMessage(expectedMessage)
    }

    @Test
    fun `should throw EssentialParamMissing when items url field is null`() {
        val response = FactsResponseRaw(
            result = listOf(
                FactRaw(
                    id = "",
                    url = null,
                    value = ""
                )
            )
        )
        val missingParams = listOf(FactRaw.URL_FIELD)
        val expectedMessage = "The $response has missing parameters. They are: $missingParams"

        assertThatExceptionOfType(EssentialParamMissing::class.java)
            .isThrownBy { factsMapper.apply(response) }
            .withMessage(expectedMessage)
    }

    @Test
    fun `should throw EssentialParamMissing when items value field is null`() {
        val response = FactsResponseRaw(
            result = listOf(
                FactRaw(
                    id = "",
                    url = "",
                    value = null
                )
            )
        )
        val missingParams = listOf(FactRaw.VALUE_FIELD)
        val expectedMessage = "The $response has missing parameters. They are: $missingParams"

        assertThatExceptionOfType(EssentialParamMissing::class.java)
            .isThrownBy { factsMapper.apply(response) }
            .withMessage(expectedMessage)
    }

    @Test
    fun `should get empty list when items is empty`() {
        val response = FactsResponseRaw(
            result = emptyList()
        )
        val result = factsMapper.apply(response)
        assertThat(result).isEqualTo(emptyList<Fact>())
    }

    @Test
    fun `should get mapped list when items is not empty`() {
        val factRaw = FactRaw(
            id = "1",
            url = "url",
            value = "value"
        )

        val response = FactsResponseRaw(
            result = listOf(factRaw)
        )

        val expected = listOf(
            Fact(
                id = factRaw.id!!,
                url = factRaw.url!!,
                value = factRaw.value!!,
                categories = emptyList()
            )
        )

        val result = factsMapper.apply(response)

        assertThat(result).isEqualTo(expected)
    }

}
