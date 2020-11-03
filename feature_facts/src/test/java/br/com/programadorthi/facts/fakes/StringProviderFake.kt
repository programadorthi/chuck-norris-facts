package br.com.programadorthi.facts.fakes

import br.com.programadorthi.domain.resource.StringProvider

class StringProviderFake(
    var textToReturn: String
) : StringProvider {
    override fun getString(stringId: Int): String = textToReturn

    override fun getString(stringId: Int, vararg args: Any): String = textToReturn
}