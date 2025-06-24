package dev.materii.gloom.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import dev.materii.gloom.shared.R

@Composable
@Suppress("ModifierMissing")
fun BackButton() {
    val nav = LocalNavigator.currentOrThrow

    if (nav.canPop) {
        IconButton(onClick = { nav.pop() }) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(R.string.action_back))
        }
    }
}