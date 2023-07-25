package org.bogucki.calllog.domain.usecases

import kotlinx.coroutines.flow.StateFlow
import org.bogucki.calllog.domain.models.CallLogEntry
import org.bogucki.calllog.domain.repositories.CallRepository
import javax.inject.Inject

class GetCallLogUseCase @Inject constructor(private val callRepository: CallRepository) {

    operator fun invoke(): StateFlow<List<CallLogEntry>> {
        return callRepository.getCallLog()
    }
}