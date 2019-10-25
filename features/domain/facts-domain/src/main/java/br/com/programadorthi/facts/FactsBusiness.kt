package br.com.programadorthi.facts

sealed class FactsBusiness : Exception() {
    object EmptySearch : FactsBusiness()
}
