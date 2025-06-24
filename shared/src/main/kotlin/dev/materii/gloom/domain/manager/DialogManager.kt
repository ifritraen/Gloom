package dev.materii.gloom.domain.manager

import android.content.Context
import dev.materii.gloom.domain.manager.base.BasePreferenceManager
import dev.materii.gloom.domain.manager.base.enumPreference

class DialogManager(context: Context):
    BasePreferenceManager(context.getSharedPreferences("dialogs", Context.MODE_PRIVATE)) {

    var downloadAsset by enumPreference("release_asset_download", DialogState.UNKNOWN)

    var installApk by enumPreference("install_apk", DialogState.UNKNOWN)

}

enum class DialogState {
    UNKNOWN,
    CONFIRMED,
    DENIED
}