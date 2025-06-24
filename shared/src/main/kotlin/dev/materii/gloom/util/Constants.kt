package dev.materii.gloom.util

import android.os.Build
import android.os.Environment
import androidx.annotation.ChecksSdkIntAtLeast
import dev.materii.gloom.shared.BuildConfig
import java.io.File

object Constants {

    const val DEV_USER_ID = "MDQ6VXNlcjQ0OTkyNTM3"
    const val DEFAULT_USERNAME = "ghost"

    object FileSize {

        const val KILO = 1024
        const val MEGA = 1024 * KILO
        const val GIGA = 1024 * MEGA
    }

}

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
val supportsMonet = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
val isDebug = BuildConfig.DEBUG

val GloomPath = File(Environment.getExternalStorageDirectory(), "Gloom")
val Features = listOf(Feature.DYNAMIC_COLOR, Feature.INSTALL_APKS, Feature.CHANGE_ICON)