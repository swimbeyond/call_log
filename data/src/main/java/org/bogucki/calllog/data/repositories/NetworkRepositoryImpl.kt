package org.bogucki.calllog.data.repositories

import org.bogucki.calllog.data.datasources.NetworkDataSource
import org.bogucki.calllog.domain.repositories.NetworkRepository

private const val HTTP_PREFIX = "http://"

internal class NetworkRepositoryImpl(private val dataSource: NetworkDataSource) :
    NetworkRepository {

    override fun getIpAddress(): String {
        return "${HTTP_PREFIX}${dataSource.getIpAddress()}:${getPortNumber()}"
    }

    override fun getPortNumber(): Int = dataSource.getPortNumber()
}