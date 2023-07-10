package org.bogucki.calllog.domain.usecases

import app.cash.turbine.test
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.bogucki.calllog.domain.models.CallLogEntry
import org.bogucki.calllog.domain.repositories.CallRepository
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant

class GetCallLogUseCaseTest {

    private val callRepositoryMock: CallRepository = mockk()

    val useCase = GetCallLogUseCase(callRepositoryMock)

    @Test
    fun `verify useCase returns correct flow from repository`() = runTest {
        val flow = MutableStateFlow(listOf<CallLogEntry>())
        every { callRepositoryMock.getCallLog() } returns flow

        useCase().test {
            assertEquals(0, awaitItem().size)
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