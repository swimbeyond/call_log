package org.bogucki.calllog.domain.usecases

import org.bogucki.calllog.domain.repositories.NetworkRepository

class GetPortNumberUseCase(private val networkRepository: NetworkRepository) {

    operator fun invoke(): Int {
        return networkRepository.getPortNumber()
    }
}