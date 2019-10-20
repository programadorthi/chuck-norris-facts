package br.com.programadorthi.network.fake

import br.com.programadorthi.network.ConnectionCheck

class ConnectionCheckFake(var hasConnection: Boolean = true) :
    ConnectionCheck {
    override fun hasInternetConnection(): Boolean = hasConnection
}
