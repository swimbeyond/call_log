package org.bogucki.calllog.domain.modules

import org.bogucki.calllog.domain.usecases.*
import org.koin.dsl.module

val domainModule = module {
    single { GetServerAddressUseCase(get()) }
    single { GetCallStatusWithCounterIncrementUseCase(get()) }
    single { SetCallStatusUseCase(get()) }
    single { GetCallLogUseCase(get()) }
    single { GetPortNumberUseCase(get()) }
}