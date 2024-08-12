package io.github.maikotrindade.stockapp.common

import java.util.Locale

object Formatter {
    fun Double.formatCurrency() = String.format(Locale.getDefault(), "$%.2f", this)
}