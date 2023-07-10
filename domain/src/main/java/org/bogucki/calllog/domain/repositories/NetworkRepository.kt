package org.bogucki.calllog.domain.repositories

interface NetworkRepository {

    fun getIpAddress(): String

    fun getPortNumber(): Int
}