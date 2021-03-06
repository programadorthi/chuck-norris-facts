package br.com.programadorthi.chucknorrisfacts.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.core.content.ContextCompat
import br.com.programadorthi.network.ConnectionCheck

class ConnectionCheckImpl(context: Context) : ConnectionCheck {

    private val service = ContextCompat.getSystemService(context, ConnectivityManager::class.java)

    override fun hasInternetConnection(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = service?.activeNetwork ?: return false
            val capabilities = service.getNetworkCapabilities(network) ?: return false
            return capabilities.run {
                hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                        hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
            }
        }
        return service?.activeNetworkInfo?.isConnectedOrConnecting ?: false
    }
}
