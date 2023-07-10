package org.bogucki.calllog.data.repositories

import androidx.annotation.VisibleForTesting
import kotlinx.coroutines.flow.StateFlow
import org.bogucki.calllog.data.datasources.CallLogDataSource
import org.bogucki.calllog.data.datasources.ContactDataSource
import org.bogucki.calllog.domain.models.CallLogEntry
import org.bogucki.calllog.domain.models.CallStatus
import org.bogucki.calllog.domain.repositories.CallRepository

internal class CallRepositoryImpl(
    private val contactDataSource: ContactDataSource,
    private val callLogDataSource: CallLogDataSource,): CallRepository {

    @VisibleForTesting
    var inCall = false
    @VisibleForTesting
    var phoneNumber: String? = null

    override fun setCallStatus(ongoing: Boolean, phoneNumber: String) {
        inCall = ongoing
        this.phoneNumber = if (ongoing) phoneNumber else null
    }


    override fun getCallStatusWithCounterIncrement(): CallStatus {
        if (inCall) callLogDataSource.incrementCounter()
        return CallStatus(inCall, phoneNumber, phoneNumber?.let { getContactName(it) })
    }

    override fun getCallLog(): StateFlow<List<CallLogEntry>> {
        return callLogDataSource.callLog
    }

    private fun getContactName(phoneNumber: String): String? =
        contactDataSource.getContactNameByPhoneNumber(phoneNumber)
}