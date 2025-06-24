package dev.materii.gloom.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.outlined.CheckCircleOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.benasher44.uuid.uuid4
import dev.materii.gloom.domain.manager.DownloadManager
import dev.materii.gloom.shared.R
import dev.materii.gloom.ui.widget.alert.LocalAlertController
import org.koin.compose.koinInject

@Composable
fun DownloadButton(
    downloadUrl: String,
    modifier: Modifier = Modifier,
    fileName: String = downloadUrl.split("/").lastOrNull() ?: "${uuid4()}.blob",
    onDownloadFinish: (String) -> Unit = {}
) {
    val downloadManager: DownloadManager = koinInject()
    val alertController = LocalAlertController.current
    val downloadingText = stringResource(R.string.msg_downloading_file, fileName)
    val downloadedText = stringResource(R.string.msg_download_completed)

    IconButton(
        onClick = {
            alertController.showText(downloadingText, icon = Icons.Filled.Download)
            downloadManager.download(downloadUrl) {
                alertController.showText(downloadedText, icon = Icons.Outlined.CheckCircleOutline)
                onDownloadFinish(it)
            }
        },
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Filled.Download,
            contentDescription = stringResource(R.string.action_download)
        )
    }
}