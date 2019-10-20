package br.com.programadorthi.network.fake

import br.com.programadorthi.network.mapper.RemoteMapper

class RemoteMapperFake(var throwException: Boolean = false) : RemoteMapper<String, Int>() {

    override fun checkEssentialParams(missingFields: MutableList<String>, raw: String) {
        if (throwException) {
            missingFields.add("")
        }
    }

    override fun copyValuesAfterEssentialParamsChecked(raw: String): Int {
        return Integer.MAX_VALUE
    }
}