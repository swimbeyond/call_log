package org.bogucki.calllog.domain.repositories

import kotlinx.coroutines.flow.StateFlow
import org.bogucki.calllog.domain.models.CallLogEntry
import org.bogucki.calllog.domain.models.CallStatus

interface CallRepository {

    fun setCallStatus(ongoing: Boolean, phoneNumber: String)

    fun getCallStatusWithCounterIncrement(): CallStatus

    fun getCallLog(): StateFlow<List<CallLogEntry>>
}