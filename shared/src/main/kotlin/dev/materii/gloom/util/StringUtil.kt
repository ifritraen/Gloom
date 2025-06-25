package dev.materii.gloom.util

import android.content.Context
import org.koin.core.context.GlobalContext

fun Context.getPluralString(res: Int, count: Int, vararg args: Any): String =
    resources.getQuantityString(res, count, *args)

fun getString(res: Int): String {
    val ctx: Context = GlobalContext.get().get()
    return ctx.getString(res)
}

fun getString(res: Int, vararg args: Any): String {
    val ctx: Context = GlobalContext.get().get()
    return ctx.getString(res, *args)
}

fun getPluralString(res: Int, count: Int, vararg args: Any): String {
    val ctx: Context = GlobalContext.get().get()
    return ctx.getPluralString(res, count, *args)
}