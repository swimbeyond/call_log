package org.bogucki.calllog.data.repositories

import app.cash.turbine.test
import io.mockk.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.bogucki.calllog.data.datasources.CallLogDataSource
import org.bogucki.calllog.data.datasources.ContactDataSource
import org.bogucki.calllog.domain.models.CallLogEntry
import org.junit.Assert.*
import org.junit.Test
import java.time.Instant

class CallRepositoryImplTest {

    private val callLogDataSourceMock: CallLogDataSource = mockk()
    private val contactDataSourceMock: ContactDataSource = mockk()

    private val callRepository = CallRepositoryImpl(contactDataSourceMock, callLogDataSourceMock)

    @Test
    fun `verify active call status sets correctly`() {
        callRepository.setCallStatus(true, "123123123")
        assertTrue(callRepository.inCall)
        assertEquals("123123123", callRepository.phoneNumber)
    }

    @Test
    fun `verify idle call status sets correctly`() {
        callRepository.setCallStatus(false, "123123123")
        assertFalse(callRepository.inCall)
        assertNull(callRepository.phoneNumber)
    }

    @Test
    fun `verify getCallStatusWithCounterIncrement returns ongoing status correctly`() {

        callRepository.inCall = true
        callRepository.phoneNumber = "123123"

        every { contactDataSourceMock.getContactNameByPhoneNumber(any()) } returns "Michael Jordan"
        every { callLogDataSourceMock.incrementCounter() } just Runs

        val status = callRepository.getCallStatusWithCounterIncrement()

        assertTrue(status.ongoing)
        assertEquals("123123", status.number)
        assertEquals("Michael Jordan", status.name)
    }

    @Test
    fun `verify getCallStatusWithCounterIncrement returns idle status correctly`() {

        callRepository.inCall = false
        callRepository.phoneNumber = null

        every { contactDataSourceMock.getContactNameByPhoneNumber(any()) } returns "Michael Jordan"
        every { callLogDataSourceMock.incrementCounter() } just Runs

        val status = callRepository.getCallStatusWithCounterIncrement()

        assertFalse(status.ongoing)
        assertNull(status.number)
        assertNull(status.name)
    }

    @Test
    fun `given ongoing call status verify getCallStatusWithCounterIncrement increments counter`() {
        callRepository.inCall = true
        callRepository.phoneNumber = "123123"

        every { contactDataSourceMock.getContactNameByPhoneNumber(any()) } returns "Michael Jordan"
        every { callLogDataSourceMock.incrementCounter() } just Runs

        callRepository.getCallStatusWithCounterIncrement()

        verify {
            callLogDataSourceMock.incrementCounter()
        }
    }

    @Test
    fun `given idle call status verify getCallStatusWithCounterIncrement doesn't increment counter`() {
        callRepository.inCall = false
        callRepository.phoneNumber = null

        every { contactDataSourceMock.getContactNameByPhoneNumber(any()) } returns "Michael Jordan"
        every { callLogDataSourceMock.incrementCounter() } just Runs

        callRepository.getCallStatusWithCounterIncrement()

        verify(inverse = true) {
            callLogDataSourceMock.incrementCounter()
        }
    }

    @Test
    fun `given response from dataSource verify repository returns call log correctly`() = runTest {
        val flow = MutableStateFlow<List<CallLogEntry>>(listOf())
        every { callLogDataSourceMock.callLog } returns flow

        callRepository.getCallLog().test {
            assertTrue(awaitItem().isEmpty())

            flow.emit(mockCallList)
            assertEquals(2, awaitItem().size)
        }
    }
}

private val mockCallList = listOf(
    CallLogEntry(
        beginning = Instant.ofEpochMilli(1000L),
        name = "John Doe",
        duration = 30,
        number = "+34123123123"
    ),
    CallLogEntry(
        beginning = Instant.ofEpochMilli(2000L),
        name = "John Smith",
        duration = 40,
        number = "+34123123124"
    )
)