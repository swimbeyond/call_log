package org.bogucki.calllog.domain.usecases

import io.mockk.*
import org.bogucki.calllog.domain.repositories.CallRepository
import org.junit.Test

class SetCallStatusUseCaseTest {

    private val callRepositoryMock: CallRepository = mockk()

    private val useCase = SetCallStatusUseCase(callRepositoryMock)

    @Test
    fun `verify that useCase sets call status correctly`() {

        every { callRepositoryMock.setCallStatus(any(), any()) } just Runs

        useCase(true, "123123123")
        verify {
            callRepositoryMock.setCallStatus(true, "123123123")
        }
    }
}