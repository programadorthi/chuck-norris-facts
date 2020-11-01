package br.com.programadorthi.facts.domain

import br.com.programadorthi.domain.ResultTypes

sealed class FactsBusiness : ResultTypes.Business() {
    object EmptySearch : FactsBusiness()
}
