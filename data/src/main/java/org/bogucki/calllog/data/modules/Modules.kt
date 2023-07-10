package org.bogucki.calllog.data.modules

import android.content.ContentResolver
import android.content.Context
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.telephony.TelephonyManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.bogucki.calllog.data.datasources.CallLogDataSource
import org.bogucki.calllog.data.datasources.ContactDataSource
import org.bogucki.calllog.data.datasources.NetworkDataSource
import org.bogucki.calllog.data.repositories.CallRepositoryImpl
import org.bogucki.calllog.data.repositories.NetworkRepositoryImpl
import org.bogucki.calllog.domain.repositories.CallRepository
import org.bogucki.calllog.domain.repositories.NetworkRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val dataModule = module {

    single(named("io")) { CoroutineScope(SupervisorJob() + Dispatchers.IO) }

    single<ContentResolver> { androidContext().contentResolver }

    single { androidContext().applicationContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager }
    single { androidContext().applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager }

    single { ContactDataSource(get()) }
    single { CallLogDataSource(get(), get(named("io"))) }
    single { NetworkDataSource(get()) }

    single<CallRepository> { CallRepositoryImpl(get(), get()) }
    single<NetworkRepository> { NetworkRepositoryImpl(get()) }
}