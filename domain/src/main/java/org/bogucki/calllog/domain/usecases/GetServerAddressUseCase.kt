package org.bogucki.calllog.domain.usecases

import org.bogucki.calllog.domain.repositories.NetworkRepository

class GetServerAddressUseCase(private val networkRepository: NetworkRepository) {

    operator fun invoke(): String {
        return networkRepository.getIpAddress()
    }
}