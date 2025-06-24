package dev.materii.gloom.ui.util

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics

inline fun Modifier.thenIf(predicate: Boolean, block: Modifier.() -> Modifier) =
    if (predicate) then(Modifier.block()) else this

@Composable
fun Modifier.contentDescription(
    @StringRes descRes: Int,
    mergeDescendants: Boolean = false
) =
    contentDescription(stringResource(descRes), mergeDescendants)

@Composable
fun Modifier.contentDescription(
    @StringRes descRes: Int,
    vararg args: Any,
    mergeDescendants: Boolean = false
) =
    contentDescription(stringResource(descRes, *args), mergeDescendants)

fun Modifier.contentDescription(desc: String, mergeDescendants: Boolean = false) =
    semantics(mergeDescendants) {
        contentDescription = desc
    }