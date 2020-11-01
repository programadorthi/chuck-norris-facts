package br.com.programadorthi.facts.domain

sealed class FactsBusiness : Exception() {
    object EmptySearch : FactsBusiness()
}
