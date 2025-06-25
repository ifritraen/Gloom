package dev.materii.gloom.ui.screen.list

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.koin.koinScreenModel
import dev.materii.gloom.api.model.ModelRepo
import dev.materii.gloom.gql.StarredReposQuery
import dev.materii.gloom.shared.R
import dev.materii.gloom.ui.screen.list.base.BaseListScreen
import dev.materii.gloom.ui.screen.list.viewmodel.StarredReposListViewModel
import dev.materii.gloom.ui.screen.repo.component.RepoItem
import org.koin.core.parameter.parametersOf

class StarredReposListScreen(
    private val username: String,
): BaseListScreen<ModelRepo, StarredReposQuery.Data?, StarredReposListViewModel>() {

    override val key: ScreenKey
        get() = "${this::class.simpleName}($username)"

    override val titleRes: Int get() = R.string.title_starred

    override val viewModel: StarredReposListViewModel
        @Composable get() = koinScreenModel { parametersOf(username) }

    @Composable
    override fun Item(item: ModelRepo) = RepoItem(repo = item)

}