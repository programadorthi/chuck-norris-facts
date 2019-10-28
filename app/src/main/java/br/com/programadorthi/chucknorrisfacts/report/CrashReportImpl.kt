package br.com.programadorthi.chucknorrisfacts.report

import br.com.programadorthi.domain.report.CrashReport
import com.orhanobut.logger.Logger

class CrashReportImpl : CrashReport {
    override fun report(cause: Throwable) {
        // Send cause to crashlytics or other crash service
        Logger.e(cause, "Crash reported")
    }
}
