package org.bogucki.calllog.domain.usecases

import org.bogucki.calllog.domain.models.CallStatus
import org.bogucki.calllog.domain.repositories.CallRepository
import javax.inject.Inject

class GetCallStatusWithCounterIncrementUseCase @Inject constructor(private val callRepository: CallRepository) {

    operator fun invoke(): CallStatus {
        return callRepository.getCallStatusWithCounterIncrement()
    }
}