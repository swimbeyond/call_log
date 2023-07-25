package org.bogucki.calllog.data.modules

import android.content.ContentResolver
import android.content.Context
import android.net.ConnectivityManager
import android.telephony.TelephonyManager
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.bogucki.calllog.data.repositories.CallRepositoryImpl
import org.bogucki.calllog.data.repositories.NetworkRepositoryImpl
import org.bogucki.calllog.domain.repositories.CallRepository
import org.bogucki.calllog.domain.repositories.NetworkRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class DataBindings {

    @Binds
    abstract fun bindCallRepository(callRepositoryImpl: CallRepositoryImpl): CallRepository

    @Binds
    abstract fun networkRepository(networkRepositoryImpl: NetworkRepositoryImpl): NetworkRepository
}

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    fun provideContentResolver(@ApplicationContext context: Context) : ContentResolver {
        return context.contentResolver
    }

    @Provides
    fun provideTelephonyManager(@ApplicationContext context: Context): TelephonyManager {
        return context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    }

    @Provides
    fun provideConnectivityManager(@ApplicationContext context: Context): ConnectivityManager {
        return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    @Provides
    fun provideCoroutineScope(): CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
}