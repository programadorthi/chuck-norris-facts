package br.com.programadorthi.domain

interface Result<out T> {
    val value: Any?
}
