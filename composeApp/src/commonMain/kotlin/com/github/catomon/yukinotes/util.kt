package com.github.catomon.yukinotes

import java.awt.Desktop
import java.net.URI
import java.time.Instant
import java.time.LocalDate
import java.util.Locale
import java.util.TimeZone

fun epochMillisToSimpleDate(epochMillis: Long): String {
    val instant = Instant.ofEpochMilli(epochMillis)
    val userTimeZone = TimeZone.getDefault()
    val localDateTime = instant.atZone(userTimeZone.toZoneId())
    val year = localDateTime.year
    return "${if (year != LocalDate.now().year) "$year." else ""}${
        localDateTime.monthValue.toString().padStart(2, '0')
    }.${localDateTime.dayOfMonth.toString().padStart(2, '0')}" + " " +
            "${
                localDateTime.hour.toString().padStart(2, '0')
            }:${localDateTime.minute.toString().padStart(2, '0')}"
}

fun openGitHub() {
    openInBrowser(URI.create("https://github.com/Catomon"))
}

fun openInBrowser(uri: URI) {
    val osName by lazy(LazyThreadSafetyMode.NONE) { System.getProperty("os.name").lowercase(Locale.getDefault()) }
    val desktop = Desktop.getDesktop()
    when {
        Desktop.isDesktopSupported() && desktop.isSupported(Desktop.Action.BROWSE) -> desktop.browse(uri)
        "mac" in osName -> Runtime.getRuntime().exec("open $uri")
        "nix" in osName || "nux" in osName -> Runtime.getRuntime().exec("xdg-open $uri")
        //else -> throw RuntimeException("cannot open $uri")
    }
}