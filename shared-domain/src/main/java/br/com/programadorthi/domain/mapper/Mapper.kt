package br.com.programadorthi.domain.mapper

interface Mapper<From, To> {
    fun map(from: From): To
}
