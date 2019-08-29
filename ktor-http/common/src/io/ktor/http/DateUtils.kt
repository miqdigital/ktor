/*
 * Copyright 2014-2019 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package io.ktor.http

import io.ktor.util.date.*

private val HTTP_DATE_FORMATS = listOf(
    "***, dd MMM YYYY hh:mm:ss zzz",
    "****, dd-MMM-YYYY hh:mm:ss zzz",
    "*** MMM d hh:mm:ss YYYY",
    "***, dd-MMM-YYYY hh:mm:ss zzz",
    "***, dd-MMM-YYYY hh-mm-ss zzz",
    "***, dd MMM YYYY hh:mm:ss zzz",
    "*** dd-MMM-YYYY hh:mm:ss zzz",
    "*** dd MMM YYYY hh:mm:ss zzz",
    "*** dd-MMM-YYYY hh-mm-ss zzz",
    "***,dd-MMM-YYYY hh:mm:ss zzz",
    "*** MMM d YYYY hh:mm:ss zzz"
)

/**
 * Convert valid http date [String] to [GMTDate] trying various http date formats from [HTTP_DATE_FORMATS]
 *
 * Note that only GMT(UTC) date is valid http date.
 */
fun String.fromHttpToGmtDate(): GMTDate = with(trim()) {
    for (format in HTTP_DATE_FORMATS) {
        try {
            val parser = GMTDateParser(format)
            return parser.parse(this@fromHttpToGmtDate)
        } catch (_: InvalidDateStringException) {
        }
    }

    error("Failed to parse date: $this")
}

/**
 * Convert [GMTDate] to valid http date [String]
 */
fun GMTDate.toHttpDate(): String = buildString {
    append("${dayOfWeek.value}, ")
    append("${dayOfMonth.padZero(2)} ")
    append("${month.value} ")
    append(year.padZero(4))
    append(" ${hours.padZero(2)}:${minutes.padZero(2)}:${seconds.padZero(2)} ")
    append("GMT")
}

private fun Int.padZero(length: Int): String = toString().padStart(length, '0')
