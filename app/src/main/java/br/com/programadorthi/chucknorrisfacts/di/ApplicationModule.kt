package br.com.programadorthi.chucknorrisfacts.di

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import br.com.programadorthi.chucknorrisfacts.preferences.PreferencesManagerImpl
import br.com.programadorthi.chucknorrisfacts.report.CrashReportImpl
import br.com.programadorthi.domain.persist.PreferencesManager
import br.com.programadorthi.domain.report.CrashReport
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import org.koin.dsl.module

val applicationModule = module {
    single<SharedPreferences> { PreferenceManager.getDefaultSharedPreferences(get<Context>()) }

    single<CrashReport> { CrashReportImpl() }

    single { Schedulers.io() }

    factory<PreferencesManager> { PreferencesManagerImpl(get()) }
}
