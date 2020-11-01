package br.com.programadorthi.chucknorrisfacts

import android.app.Application
import br.com.programadorthi.chucknorrisfacts.di.applicationModule
import br.com.programadorthi.chucknorrisfacts.di.networkModule
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class FactsApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        initDependencyInjection()
        initLogger()
    }

    private fun initDependencyInjection() {
        startKoin {
            androidContext(this@FactsApplication)
            modules(
                listOf(
                    applicationModule,
                    networkModule
                )
            )
        }
    }

    private fun initLogger() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            Logger.addLogAdapter(AndroidLogAdapter())
        }
    }
}
