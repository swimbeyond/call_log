package org.bogucki.calllog.domain.usecases

import org.bogucki.calllog.domain.repositories.NetworkRepository
import javax.inject.Inject

class GetServerAddressUseCase @Inject constructor(private val networkRepository: NetworkRepository) {

    operator fun invoke(): String {
        return networkRepository.getIpAddress()
    }
}