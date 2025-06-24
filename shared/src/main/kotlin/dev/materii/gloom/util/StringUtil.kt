package dev.materii.gloom.util

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import dev.icerock.moko.resources.PluralsResource
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.desc.PluralFormatted
import dev.icerock.moko.resources.desc.ResourceFormatted
import dev.icerock.moko.resources.desc.StringDesc
import org.koin.core.context.GlobalContext

fun Context.getString(res: StringResource): String = res.getString(this)
fun Context.getString(res: StringResource, vararg args: Any): String =
    StringDesc.ResourceFormatted(res, *args).toString(this)

fun Context.getPluralString(res: PluralsResource, count: Int, vararg args: Any): String =
    StringDesc.PluralFormatted(res, count, *args).toString(this)

fun getString(res: Int): String {
    val ctx: Context = GlobalContext.get().get()
    return ctx.getString(res)
}

fun getString(res: Int, vararg args: Any): String {
    val ctx: Context = GlobalContext.get().get()
    return ctx.getString(res, *args)
}

fun getPluralString(res: PluralsResource, count: Int, vararg args: Any): String {
    val ctx: Context = GlobalContext.get().get()
    return ctx.getPluralString(res, count, *args)
}

@Composable
fun pluralStringResource(res: PluralsResource, count: Int, vararg args: Any) =
    StringDesc.PluralFormatted(res, count, *args).toString(LocalContext.current)