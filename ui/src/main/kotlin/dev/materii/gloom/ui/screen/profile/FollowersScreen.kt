package dev.materii.gloom.ui.screen.profile

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.koin.koinScreenModel
import dev.materii.gloom.api.model.ModelUser
import dev.materii.gloom.gql.FollowersQuery
import dev.materii.gloom.shared.R
import dev.materii.gloom.ui.screen.list.base.BaseListScreen
import dev.materii.gloom.ui.screen.profile.viewmodel.FollowersViewModel
import dev.materii.gloom.ui.widget.user.UserItem
import org.koin.core.parameter.parametersOf

class FollowersScreen(
    private val username: String,
): BaseListScreen<ModelUser, FollowersQuery.Data?, FollowersViewModel>() {

    override val key: ScreenKey
        get() = "${this::class.simpleName}($username)"

    override val titleRes: Int get() = R.string.title_followers

    override val viewModel: FollowersViewModel
        @Composable get() = koinScreenModel { parametersOf(username) }

    @Composable
    override fun Item(item: ModelUser) = UserItem(user = item)

}