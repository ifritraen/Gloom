package dev.materii.gloom.util

import android.icu.number.Notation
import android.icu.number.NumberFormatter
import android.icu.text.CompactDecimalFormat
import android.os.Build
import java.text.DecimalFormat
import java.util.Locale

object NumberFormatter {

    /**
     * Formats the given [count] into a localized compact format
     *
     * Ex.
     *
     * 1102 -> 1.1K
     *
     * 12352210 -> 12.4M
     *
     * @param count The number to make compact
     * @return A compact version of a number
     */
    fun compact(count: Int): String {
        return when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
                NumberFormatter.withLocale(Locale.getDefault())
                    .notation(Notation.compactShort())
                    .format(count)
                    .toString()
            }

            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> {
                CompactDecimalFormat.getInstance(
                    Locale.getDefault(),
                    CompactDecimalFormat.CompactStyle.SHORT
                )
                    .format(count)
            }

            else -> DecimalFormat().format(count)
        }
    }

}