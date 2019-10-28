package br.com.programadorthi.facts

data class Fact(
    val id: String,
    val url: String,
    val value: String,
    val categories: List<String>
)
