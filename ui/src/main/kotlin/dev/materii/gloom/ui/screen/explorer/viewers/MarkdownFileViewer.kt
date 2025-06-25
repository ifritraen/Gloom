package dev.materii.gloom.ui.screen.explorer.viewers

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.materii.gloom.gql.fragment.RawMarkdownFile
import dev.materii.gloom.gql.fragment.RepoFile
import dev.materii.gloom.shared.R
import dev.materii.gloom.ui.component.ErrorMessage
import dev.materii.gloom.ui.widget.markdown.Markdown

@Composable
@Suppress("ModifierMissing")
fun MarkdownFileViewer(
    markdownFile: RepoFile.OnMarkdownFileType,
    rawFile: RawMarkdownFile? = null,
    showRaw: Boolean = false,
    rawHasError: Boolean = false,
    linesSelected: IntRange? = null,
    onHideToggle: () -> Unit = {},
    onSelectLines: (lineNumbers: IntRange?, snippet: String) -> Unit = { _, _ -> }
) {
    when {
        showRaw -> {
            if (rawHasError) {
                ErrorMessage(
                    message = stringResource(R.string.msg_raw_markdown_fail),
                )
            } else {
                rawFile?.contentRaw?.let { raw ->
                    TextFileViewer(
                        content = raw,
                        extension = "md",
                        linesSelected = linesSelected,
                        onHideToggle = onHideToggle,
                        onSelectLines = onSelectLines
                    )
                }
            }
        }

        else    -> {
            markdownFile.contentHTML?.let {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    Markdown(
                        html = it,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}