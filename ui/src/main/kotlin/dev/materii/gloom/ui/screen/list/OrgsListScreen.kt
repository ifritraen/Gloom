package dev.materii.gloom.ui.screen.list

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.koin.koinScreenModel
import dev.materii.gloom.api.model.ModelUser
import dev.materii.gloom.gql.JoinedOrgsQuery
import dev.materii.gloom.shared.R
import dev.materii.gloom.ui.screen.list.base.BaseListScreen
import dev.materii.gloom.ui.screen.list.viewmodel.OrgListViewModel
import dev.materii.gloom.ui.widget.user.UserItem
import org.koin.core.parameter.parametersOf

class OrgsListScreen(
    private val username: String,
): BaseListScreen<ModelUser, JoinedOrgsQuery.Data?, OrgListViewModel>() {

    override val key: ScreenKey
        get() = "${this::class.simpleName}($username)"

    override val titleRes: Int get() = R.string.title_orgs

    override val viewModel: OrgListViewModel
        @Composable get() = koinScreenModel { parametersOf(username) }

    @Composable
    override fun Item(item: ModelUser) = UserItem(user = item)

}