package org.bogucki.calllog.presentation.fragments

import app.cash.turbine.test
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.bogucki.calllog.MainDispatcherRule
import org.bogucki.calllog.domain.models.CallLogEntry
import org.bogucki.calllog.domain.usecases.GetCallLogUseCase
import org.bogucki.calllog.domain.usecases.GetServerAddressUseCase
import org.bogucki.calllog.presentation.fragments.main.MainViewModel
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import java.time.Instant

class MainViewModelTest {

    @get:Rule
    val coroutineRule = MainDispatcherRule()

    private val mockCallLogUseCase = mockk<GetCallLogUseCase>()
    private val mockGetServerAddressUseCase = mockk<GetServerAddressUseCase>()

    private val mainViewModel = MainViewModel(mockCallLogUseCase, mockGetServerAddressUseCase)

    @Test
    fun `given an ipAddress returned by the useCase verify that state is updated accordingly`() =
        runTest {
            val ip = "127.0.0.1"
            every { mockGetServerAddressUseCase() } returns ip

            mainViewModel.state.test {
                assertEquals(null, awaitItem().ipAddress)
                mainViewModel.getIpAddress()
                assertEquals(ip, awaitItem().ipAddress)
            }
        }

    @Test
    fun `verify state is following the data flow correctly`() = runTest {
        val flow = MutableStateFlow(mockCallList.subList(0,1))
        every { mockCallLogUseCase.invoke() } returns flow
        mainViewModel.observeCallLog()
        mainViewModel.state.test {
            assertEquals(1, awaitItem().callLog.size)
            flow.emit(mockCallList)
            assertEquals(2, awaitItem().callLog.size)
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