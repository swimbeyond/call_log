package org.bogucki.calllog.modules

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.time.Instant

private val gson = GsonBuilder().registerTypeAdapter(
    Instant::class.java,
    object : TypeAdapter<Instant>() {
        override fun write(out: JsonWriter?, value: Instant?) {
            out?.value(value.toString())
        }

        override fun read(`in`: JsonReader?): Instant {
            return Instant.parse(`in`?.nextString())
        }

    }).create()

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun provideGson() : Gson = gson
}

//val appModule = module {
//    single(named("main")) { CoroutineScope(SupervisorJob() + Dispatchers.Main) }
//    single(named("io")) { CoroutineScope(SupervisorJob() + Dispatchers.IO) }
//
//
//    single<Gson> { gson }
//
//    viewModel { MainViewModel(get(), get()) }
//}