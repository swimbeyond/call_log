package org.bogucki.calllog.modules

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.bogucki.calllog.presentation.fragments.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
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

val appModule = module {
    single(named("main")) { CoroutineScope(SupervisorJob() + Dispatchers.Main) }
    single(named("io")) { CoroutineScope(SupervisorJob() + Dispatchers.IO) }


    single<Gson> { gson }

    viewModel { MainViewModel(get(), get()) }
}