package dev.materii.gloom.ui.screen.settings.component.account

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import dev.materii.gloom.shared.R

@Composable
fun SignOutDialog(
    signedOut: Boolean,
    onSignedOut: () -> Unit,
    onDismiss: () -> Unit,
    onSignOutClick: () -> Unit
) {
    if (signedOut) {
        onSignedOut()
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.settings_sign_out_header)) },
        text = { Text(stringResource(R.string.settings_sign_out_text)) },
        confirmButton = {
            Button(
                onClick = onSignOutClick
            ) {
                Text(stringResource(R.string.action_sign_out))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.dismiss_nevermind))
            }
        }
    )
}