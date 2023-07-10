package org.bogucki.calllog.data.datasources

import android.net.ConnectivityManager
import android.net.LinkProperties

private const val PORT_NUMBER = 8080

internal class NetworkDataSource(private val connectivityManager: ConnectivityManager) {

    fun getIpAddress(): String {
        val linkProperties =
            connectivityManager.getLinkProperties(connectivityManager.activeNetwork) as LinkProperties
        val addresses = linkProperties.linkAddresses
        return addresses.first { it.address.hostAddress.contains(".") }.address.hostAddress
            ?: "0.0.0.0"
    }

    fun getPortNumber(): Int = PORT_NUMBER
}