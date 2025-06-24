package dev.materii.gloom.ui.screen.settings.about

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import dev.materii.gloom.shared.R
import dev.materii.gloom.ui.component.toolbar.LargeToolbar
import dev.materii.gloom.ui.screen.settings.component.SettingsCategory
import dev.materii.gloom.ui.screen.settings.component.SettingsGroup

class AboutScreen: Screen {

    @Composable
    @OptIn(ExperimentalMaterial3Api::class)
    override fun Content() {
        val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

        Scaffold(
            topBar = { Toolbar(scrollBehavior) },
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
        ) { pv ->
            Column(
                modifier = Modifier
                    .padding(pv)
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp)
            ) {
                SettingsGroup {
                    SettingsCategory(
                        icon = Icons.Outlined.Book,
                        text = stringResource(R.string.settings_libraries),
                        subtext = stringResource(R.string.settings_libraries_description),
                        destination = ::LibrariesScreen
                    )
                }
            }
        }
    }

    @Composable
    @OptIn(ExperimentalMaterial3Api::class)
    private fun Toolbar(
        scrollBehavior: TopAppBarScrollBehavior
    ) {
        LargeToolbar(
            title = stringResource(R.string.settings_about),
            scrollBehavior = scrollBehavior
        )
    }

}