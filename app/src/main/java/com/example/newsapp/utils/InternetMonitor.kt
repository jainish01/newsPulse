package com.example.newsapp.utils

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Singleton

@Singleton
interface InternetMonitor {
    var isAvailable: Boolean
    val availabilityFlow: Flow<Boolean>
}

class InternetMonitorImpl(connectivityManager: ConnectivityManager
): InternetMonitor {

    private val _availabilityFlow: MutableStateFlow<Boolean> = MutableStateFlow(false)
    override val availabilityFlow: Flow<Boolean> = _availabilityFlow

    override var isAvailable: Boolean = false
        set(value) {
            field = value
            _availabilityFlow.value = value
        }

    private var isWifiAvailable: Boolean = false
        set(value) {
            field = value
            setInternetAvailability()
        }

    private var isMobileAvailable: Boolean = false
        set(value) {
            field = value
            setInternetAvailability()
        }

    init {
        val activeNetwork = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        isWifiAvailable = capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true
        isMobileAvailable = capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true
        setInternetAvailability()

        val mobileCallback: ConnectivityManager.NetworkCallback = object :
            ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                Log.i(TAG, "onAvailable mobile: $network")
                isMobileAvailable = true
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                Log.i(TAG, "onLost mobile: $network")
                isMobileAvailable = false
            }
        }

        connectivityManager.registerNetworkCallback(
            NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .build(), mobileCallback
        )

        val wifiCallback: ConnectivityManager.NetworkCallback = object :
            ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                Log.i(TAG, "onAvailable wifi: $network")
                isWifiAvailable = true
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                Log.i(TAG, "onLost wifi: $network")
                isWifiAvailable = false
            }
        }

        connectivityManager.registerNetworkCallback(
            NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .build(), wifiCallback
        )
    }


    private fun setInternetAvailability() {
        isAvailable = isMobileAvailable || isWifiAvailable
    }

    companion object {
        private const val TAG = "InternetMonitor"
    }
}