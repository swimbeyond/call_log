package org.bogucki.calllog.application

import android.app.Application
import org.bogucki.calllog.BuildConfig
import org.bogucki.calllog.data.modules.dataModule
import org.bogucki.calllog.domain.modules.domainModule
import org.bogucki.calllog.modules.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class CallLogApplication : Application(), KoinComponent {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@CallLogApplication)
            modules(appModule, domainModule, dataModule)
        }
    }
}