package org.bogucki.calllog.domain.usecases

import io.mockk.every
import io.mockk.mockk
import org.bogucki.calllog.domain.models.CallStatus
import org.bogucki.calllog.domain.repositories.CallRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GetCallStatusWithCounterIncrementUseCaseTest {

    private val callRepositoryMock: CallRepository = mockk()

    private val useCase = GetCallStatusWithCounterIncrementUseCase(callRepositoryMock)

    @Test
    fun `verify useCase returns call state correctly`() {
        every { callRepositoryMock.getCallStatusWithCounterIncrement() } returns mockCallStatus

        assertTrue(useCase().ongoing)
        assertEquals("John Doe", useCase().name)
        assertEquals("123123123", useCase().number)
    }
}

private val mockCallStatus = CallStatus(
    ongoing = true,
    name = "John Doe",
    number = "123123123"
)