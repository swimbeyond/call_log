package org.bogucki.calllog.domain.usecases

import io.mockk.every
import io.mockk.mockk
import org.bogucki.calllog.domain.repositories.NetworkRepository
import org.junit.Assert.assertEquals
import org.junit.Test

class GetPortNumberUseCaseTest {

    private val networkRepositoryMock: NetworkRepository = mockk()

    private val useCase = GetPortNumberUseCase(networkRepositoryMock)

    @Test
    fun `given value from repository verify useCase returns correct port number`() {
        every { networkRepositoryMock.getPortNumber() } returns 12345

        assertEquals(12345, useCase())
    }
}