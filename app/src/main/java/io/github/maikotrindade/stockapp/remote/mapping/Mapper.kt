package io.github.maikotrindade.stockapp.mapping

interface Mapper<Local, Remote> {
    fun fromRemote(stock: Remote): Local
    fun fromLocal(stock: Local): Remote
}