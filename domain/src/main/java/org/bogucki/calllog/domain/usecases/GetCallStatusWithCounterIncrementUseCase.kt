package org.bogucki.calllog.domain.usecases

import org.bogucki.calllog.domain.models.CallStatus
import org.bogucki.calllog.domain.repositories.CallRepository

class GetCallStatusWithCounterIncrementUseCase(private val callRepository: CallRepository) {

    operator fun invoke(): CallStatus {
        return callRepository.getCallStatusWithCounterIncrement()
    }
}