package org.bogucki.calllog.domain.usecases

import org.bogucki.calllog.domain.repositories.CallRepository
import javax.inject.Inject

class SetCallStatusUseCase @Inject constructor(private val callRepository: CallRepository) {

    operator fun invoke(ongoing: Boolean, phoneNumber: String) {
        callRepository.setCallStatus(ongoing, phoneNumber)
    }
}