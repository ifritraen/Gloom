package dev.materii.gloom.ui.screen.repo

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.koin.koinScreenModel
import dev.materii.gloom.api.model.ModelUser
import dev.materii.gloom.gql.RepoContributorsQuery
import dev.materii.gloom.shared.R
import dev.materii.gloom.ui.screen.list.base.BaseListScreen
import dev.materii.gloom.ui.screen.repo.viewmodel.RepoContributorsViewModel
import dev.materii.gloom.ui.widget.user.UserItem
import org.koin.core.parameter.parametersOf

class ContributorsScreen(
    private val owner: String,
    private val repository: String
): BaseListScreen<ModelUser, RepoContributorsQuery.Data?, RepoContributorsViewModel>() {

    override val titleRes: Int get() = R.string.title_contributors

    override val key: ScreenKey
        get() = "${this::class.simpleName}($owner, $repository)"

    override val viewModel: RepoContributorsViewModel
        @Composable
        get() = koinScreenModel { parametersOf(owner, repository) }

    @Composable
    override fun Item(item: ModelUser) = UserItem(item)

}