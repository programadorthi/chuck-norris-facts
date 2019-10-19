package br.com.programadorthi.domain.report

interface CrashReport {
    fun report(cause: Throwable)
}
