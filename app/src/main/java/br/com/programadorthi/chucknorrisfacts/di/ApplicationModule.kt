package br.com.programadorthi.chucknorrisfacts.di

import android.content.SharedPreferences
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import br.com.programadorthi.chucknorrisfacts.factory.ViewModelFactory
import br.com.programadorthi.chucknorrisfacts.preferences.PreferencesManagerImpl
import br.com.programadorthi.chucknorrisfacts.report.CrashReportImpl
import br.com.programadorthi.domain.persist.PreferencesManager
import br.com.programadorthi.domain.report.CrashReport
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider
import org.kodein.di.singleton

val applicationModule = DI.Module("applicationModule") {
    bind<SharedPreferences>() with singleton {
        PreferenceManager.getDefaultSharedPreferences(
            instance()
        )
    }
    bind<CrashReport>() with singleton { CrashReportImpl() }
    bind<Scheduler>() with singleton { Schedulers.io() }
    bind<PreferencesManager>() with singleton { PreferencesManagerImpl(instance()) }
    bind<ViewModelProvider.Factory>() with provider { ViewModelFactory(di) }
}
