package dev.materii.gloom.util

import android.content.Context
import dev.materii.gloom.shared.R
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object EmojiUtil: KoinComponent {

    private val json: Json by inject()
    private val context: Context by inject()

    private var _emojis: Map<String, String>? = null

    val emojis: Map<String, String> by lazy {
        val emojiStream = context.resources.openRawResource(R.raw.emoji)
        val text = String(emojiStream.readBytes())

        json.decodeFromString<Map<String, String>>(text)
    }

}