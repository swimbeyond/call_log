package org.bogucki.calllog.domain.usecases

import io.mockk.every
import io.mockk.mockk
import org.bogucki.calllog.domain.repositories.NetworkRepository
import org.junit.Assert.assertEquals
import org.junit.Test

class GetServerAddressUseCaseTest {

    private val networkRepositoryMock: NetworkRepository = mockk()

    private val useCase = GetServerAddressUseCase(networkRepositoryMock)

    @Test
    fun `given value from repository verify useCase returns correct ip address`() {

        every { networkRepositoryMock.getIpAddress() } returns "127.0.0.1"

        assertEquals("127.0.0.1", useCase())
    }
}