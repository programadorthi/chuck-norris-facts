package br.com.programadorthi.network.fake

import br.com.programadorthi.domain.report.CrashReport

class CrashReportFake : CrashReport {
    var reported: Throwable? = null
        private set

    override fun report(cause: Throwable) {
        reported = cause
    }
}
