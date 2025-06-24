package dev.materii.gloom.util

import com.mikepenz.aboutlibraries.entity.Library
import java.io.File

inline fun String?.ifNullOrBlank(block: () -> String) = if (isNullOrBlank()) block() else this

val Library.author: String
    get() = developers.takeIf { it.isNotEmpty() }?.joinToString(", ") { it.name.toString() } ?: organization?.name ?: ""

val IsDeveloper = isDebug || File(GloomPath, ".dev").exists()

// Gets overwritten in the platform specific entry point
var VersionName = "UNKNOWN"