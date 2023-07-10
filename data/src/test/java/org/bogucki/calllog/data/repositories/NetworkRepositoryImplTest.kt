package org.bogucki.calllog.data.repositories

import io.mockk.every
import io.mockk.mockk
import org.bogucki.calllog.data.datasources.NetworkDataSource
import org.junit.Assert.assertEquals
import org.junit.Test

class NetworkRepositoryImplTest {

    private val networkDataSourceMock: NetworkDataSource = mockk()

    private val repository = NetworkRepositoryImpl(networkDataSourceMock)

    @Test
    fun `given response from data source verify that repository return correct port number`() {
        every { networkDataSourceMock.getPortNumber() } returns 17

        assertEquals(17, repository.getPortNumber())
    }

    @Test
    fun `given response from data source verify that repository returns correct ip address`() {
        every { networkDataSourceMock.getPortNumber() } returns 19
        every { networkDataSourceMock.getIpAddress() } returns "127.1.1.2"

        assertEquals("http://127.1.1.2:19", repository.getIpAddress())
    }
}