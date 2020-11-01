package br.com.programadorthi.chucknorrisfacts

import android.app.Application
import android.content.Context
import br.com.programadorthi.chucknorrisfacts.di.applicationModule
import br.com.programadorthi.chucknorrisfacts.di.networkModule
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.bind
import org.kodein.di.provider
import timber.log.Timber

class FactsApplication : Application(), DIAware {
    override val di: DI by DI.lazy {
        import(applicationModule)
        import(networkModule)
        bind<Context>() with provider { this@FactsApplication }
    }

    override fun onCreate() {
        super.onCreate()
        initLogger()
    }

    private fun initLogger() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            Logger.addLogAdapter(AndroidLogAdapter())
        }
    }
}
