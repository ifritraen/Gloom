package dev.materii.gloom.ui.screen.profile

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HowToReg
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.AsyncImage
import dev.materii.gloom.api.dto.user.User
import dev.materii.gloom.api.model.ModelRepo
import dev.materii.gloom.api.model.ModelStatus
import dev.materii.gloom.api.model.ModelUser
import dev.materii.gloom.domain.manager.ShareManager
import dev.materii.gloom.gql.type.SocialAccountProvider
import dev.materii.gloom.shared.R
import dev.materii.gloom.ui.component.Avatar
import dev.materii.gloom.ui.component.BackButton
import dev.materii.gloom.ui.component.BadgedItem
import dev.materii.gloom.ui.icon.Social
import dev.materii.gloom.ui.icon.social.*
import dev.materii.gloom.ui.screen.list.OrgsListScreen
import dev.materii.gloom.ui.screen.list.RepositoryListScreen
import dev.materii.gloom.ui.screen.list.SponsoringScreen
import dev.materii.gloom.ui.screen.list.StarredReposListScreen
import dev.materii.gloom.ui.screen.profile.component.ContributionGraph
import dev.materii.gloom.ui.screen.profile.viewmodel.ProfileViewModel
import dev.materii.gloom.ui.screen.repo.RepoScreen
import dev.materii.gloom.ui.screen.repo.component.RepoItem
import dev.materii.gloom.ui.screen.settings.SettingsScreen
import dev.materii.gloom.ui.util.NavigationUtil.navigate
import dev.materii.gloom.ui.widget.alert.LocalAlertController
import dev.materii.gloom.ui.widget.markdown.ReadMeCard
import dev.materii.gloom.util.Constants
import dev.materii.gloom.util.EmojiUtil
import dev.materii.gloom.util.NumberFormatter
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf
import java.net.URI

open class ProfileScreen(
    val user: String = ""
): Screen {

    override val key: ScreenKey
        get() = "${this::class.simpleName}($user)"

    @Composable
    @OptIn(ExperimentalMaterial3Api::class)
    override fun Content() {
        val viewModel: ProfileViewModel = koinScreenModel { parametersOf(user) }
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = { TopBar(viewModel, scrollBehavior) }
        ) { pv ->
            PullToRefreshBox(
                isRefreshing = viewModel.isLoading,
                onRefresh = { viewModel.loadData() },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(pv)
            ) {
                Column(
                    Modifier
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    viewModel.user?.let { user ->
                        Header(user = user)

                        user.readme?.let { readme ->
                            Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                                val nav = LocalNavigator.currentOrThrow
                                val repository = viewModel.user!!.username ?: Constants.DEFAULT_USERNAME

                                ReadMeCard(
                                    text = readme,
                                    repository = repository,
                                    onClickRepository = {
                                        if (user.type == User.Type.USER) {
                                            nav.navigate(RepoScreen(user.username!!, repository))
                                        }
                                    }
                                )
                            }
                        }

                        if (user.pinnedItems.isNotEmpty()) PinnedItems(pinned = user.pinnedItems.toImmutableList())

                        user.contributions?.let {
                            ContributionGraph(calendar = it.contributionCalendar)
                        }

                        StatCard(
                            isOrg = user.type == User.Type.ORG,
                            repoCount = user.repos ?: 0L,
                            orgCount = user.orgs ?: 0L,
                            starCount = user.starred ?: 0L,
                            sponsoringCount = user.sponsoring ?: 0L,
                            username = user.username
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }

    @Composable
    @OptIn(ExperimentalMaterial3Api::class)
    private fun TopBar(
        viewModel: ProfileViewModel,
        scrollBehavior: TopAppBarScrollBehavior
    ) {
        val nav = LocalNavigator.current
        val shareManager: ShareManager = koinInject()

        TopAppBar(
            title = {
                val opacity = scrollBehavior.state.overlappedFraction
                val textColor by animateColorAsState(
                    targetValue = MaterialTheme.colorScheme.onSurface.copy(
                        alpha = opacity
                    ),
                    animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                    label = "Username fade"
                )
                Text(
                    text = viewModel.user?.username ?: "",
                    color = textColor
                )
            },
            navigationIcon = { BackButton() },
            scrollBehavior = scrollBehavior,
            actions = {
                if (!viewModel.user?.username.isNullOrBlank()) {
                    IconButton(onClick = { shareManager.shareText("https://github.com/${viewModel.user!!.username}") }) {
                        Icon(Icons.Filled.Share, stringResource(R.string.action_share))
                    }
                }

                FollowButton(viewModel)

                if (user.isBlank()) {
                    IconButton(onClick = { nav?.navigate(SettingsScreen()) }) {
                        Icon(
                            Icons.Outlined.Settings,
                            stringResource(R.string.navigation_settings)
                        )
                    }
                }
            }
        )
    }

    @OptIn(ExperimentalLayoutApi::class)
    @Composable
    private fun Header(
        user: ModelUser
    ) {
        val nav = LocalNavigator.current

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            ProfileAvatar(user)

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(5.dp))
                if (user.displayName != null) {
                    Text(
                        text = user.displayName!!,
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = user.username ?: "ghost",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontStyle = FontStyle.Italic,
                            color = MaterialTheme.colorScheme.onSurface.copy(0.5f)
                        )
                    )
                } else {
                    Text(
                        text = user.username ?: "ghost",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            if (user.isFollowingYou) {
                Text(
                    text = stringResource(R.string.label_follows_you),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp))
                        .padding(horizontal = 9.dp, vertical = 5.dp)
                )
            }

            user.status?.let {
                Status(it)
            }

            user.bio?.let {
                if (it.isNotEmpty()) Text(
                    text = it,
                    textAlign = TextAlign.Center
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterHorizontally)
                ) {
                    user.company?.let {
                        ProfileDetail(
                            text = it,
                            icon = { Icon(Icons.Outlined.Business, contentDescription = null) }
                        )
                    }

                    user.location?.let {
                        ProfileDetail(
                            text = it,
                            icon = { Icon(Icons.Outlined.Place, contentDescription = null) }
                        )
                    }

                    user.website?.let { site ->
                        if (site == "null") return@let
                        ProfileDetail(
                            text = site,
                            icon = { Icon(Icons.Outlined.Link, contentDescription = null) }
                        ) {
                            val url =
                                if (site.startsWith("http://") || site.startsWith("https://")) site else "http://${site}"
                            it.openUri(url)
                        }
                    }

                    user.socials.forEach { social ->
                        val (icon, cdRes) = when (social.provider) {
                            SocialAccountProvider.TWITTER -> Icons.Social.Twitter to R.string.cd_twitter
                            SocialAccountProvider.YOUTUBE -> Icons.Social.YouTube to R.string.cd_youtube
                            SocialAccountProvider.MASTODON -> Icons.Social.Mastodon to R.string.cd_mastodon
                            SocialAccountProvider.HOMETOWN -> Icons.Social.Hometown to R.string.cd_hometown
                            SocialAccountProvider.FACEBOOK -> Icons.Social.Facebook to R.string.cd_facebook
                            SocialAccountProvider.INSTAGRAM -> Icons.Social.Instagram to R.string.cd_instagram
                            SocialAccountProvider.LINKEDIN -> Icons.Social.LinkedIn to R.string.cd_linkedin
                            SocialAccountProvider.REDDIT -> Icons.Social.Reddit to R.string.cd_reddit
                            SocialAccountProvider.TWITCH -> Icons.Social.Twitch to R.string.cd_twitch
                            SocialAccountProvider.BLUESKY -> Icons.Social.Bluesky to R.string.cd_bluesky
                            SocialAccountProvider.NPM -> Icons.Social.NPM to R.string.cd_npm
                            else -> Icons.Outlined.Link to R.string.cd_link
                        }
                        val socialName = remember {
                            if (social.provider == SocialAccountProvider.GENERIC) {
                                val link = URI.create(social.url)
                                "${link.authority.replaceFirst("www.", "")}${link.path}"
                            } else social.displayName
                        }

                        ProfileDetail(
                            text = socialName,
                            icon = {
                                Icon(
                                    icon,
                                    contentDescription = stringResource(cdRes)
                                )
                            }
                        ) {
                            it.openUri(social.url)
                        }
                    }

                    user.email?.let { email ->
                        if (email.isBlank()) return@let
                        ProfileDetail(
                            text = email,
                            icon = { Icon(Icons.Outlined.Email, contentDescription = null) }
                        ) {
                            it.openUri("mailto:$email")
                        }
                    }
                }

                if (user.type == User.Type.USER) {
                    Row {
                        TextButton(onClick = {
                            if (user.username?.isNotBlank() == true) nav?.navigate(
                                FollowersScreen(
                                    user.username!!
                                )
                            )
                        }) {
                            Text(
                                pluralStringResource(
                                    R.plurals.followers,
                                    (user.followers ?: 0).toInt(),
                                    NumberFormatter.compact((user.followers ?: 0).toInt())
                                )
                            )
                        }
                        TextButton(onClick = {
                            if (user.username?.isNotBlank() == true) nav?.navigate(
                                FollowingScreen(
                                    user.username!!
                                )
                            )
                        }) {
                            Text(
                                pluralStringResource(
                                    R.plurals.following,
                                    (user.following ?: 0).toInt(),
                                    NumberFormatter.compact((user.following ?: 0).toInt())
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun ProfileAvatar(user: ModelUser) {
        val alertController = LocalAlertController.current
        val (badge, msg) =
            if (user.isSupporter) painterResource(R.drawable.img_badge_sponsor) to stringResource(
                R.string.badge_supporter
            )
            else if (user.id == dev.materii.gloom.util.Constants.DEV_USER_ID) painterResource(R.drawable.img_badge_dev) to stringResource(
                R.string.badge_supporter
            )
            else null to null

        BadgedItem(
            badge =
                if (badge != null && msg != null) { ->
                    Image(
                        painter = badge,
                        contentDescription = msg,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { alertController.showText(msg) }
                    )
                } else null
        ) {
            Avatar(
                url = user.avatar,
                contentDescription = stringResource(
                    R.string.noun_users_avatar,
                    user.displayName ?: "ghost"
                ),
                type = user.type,
                modifier = Modifier.size(90.dp)
            )
        }
    }

    @Composable
    private fun Status(
        status: ModelStatus
    ) {
        if (status.emoji == null && status.message == null) return
        Box(modifier = Modifier.padding(10.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp))
                    .padding(5.dp)
            ) {
                if (status.emoji != null)
                    AsyncImage(
                        model = EmojiUtil.emojis[status.emoji!!.replace(":", "")],
                        contentDescription = status.emoji,
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp))
                            .size(30.dp)
                            .padding(6.dp)
                    )

                if (status.message != null)
                    Text(
                        text = status.message!!,
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )
            }
        }
    }

    @Composable
    private fun ProfileDetail(
        text: String,
        icon: @Composable (() -> Unit)? = null,
        onClick: (UriHandler) -> Unit = {}
    ) {
        val uriHandler = LocalUriHandler.current
        val contentColor = MaterialTheme.colorScheme.primary

        Surface(
            contentColor = contentColor,
            modifier = Modifier
                .clip(CircleShape)
                .clickable {
                    onClick(uriHandler)
                }
        ) {
            CompositionLocalProvider(LocalContentColor provides contentColor) {
                ProvideTextStyle(value = MaterialTheme.typography.labelLarge) {
                    Row(
                        Modifier
                            .padding(vertical = 5.dp, horizontal = 8.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        icon?.invoke()
                        Spacer(modifier = Modifier.width(ButtonDefaults.IconSpacing))
                        Text(text)
                    }
                }
            }
        }
    }

    @Composable
    private fun StatCard(
        username: String?,
        isOrg: Boolean,
        repoCount: Long,
        orgCount: Long,
        starCount: Long,
        sponsoringCount: Long
    ) {
        val nav = LocalNavigator.current
        Box(modifier = Modifier.padding(horizontal = 16.dp)) {
            ElevatedCard {
                Column {
                    StatItem(
                        label = stringResource(R.string.title_repos),
                        count = repoCount,
                        icon = Icons.Outlined.Book
                    ) {
                        username?.let { RepositoryListScreen(it) }?.let { nav?.navigate(it) }
                    }
                    if (!isOrg) {
                        StatItem(
                            label = stringResource(R.string.title_orgs),
                            count = orgCount,
                            icon = Icons.Outlined.Business
                        ) {
                            username?.let { OrgsListScreen(it) }?.let { nav?.navigate(it) }
                        }
                        StatItem(
                            label = stringResource(R.string.title_starred),
                            count = starCount,
                            icon = Icons.Outlined.Star
                        ) {
                            username?.let { StarredReposListScreen(it) }?.let { nav?.navigate(it) }
                        }
                    }
                    if (sponsoringCount > 0) {
                        StatItem(
                            label = stringResource(R.string.title_sponsoring),
                            count = sponsoringCount,
                            icon = Icons.Outlined.FavoriteBorder
                        ) {
                            username?.let { SponsoringScreen(it) }?.let { nav?.navigate(it) }
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun StatItem(
        label: String,
        count: Long,
        icon: ImageVector,
        onClick: (() -> Unit)? = null
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable { onClick?.invoke() }
                .padding(14.dp)
        ) {
            Icon(
                icon, null,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(MaterialTheme.colorScheme.surfaceColorAtElevation(15.dp))
                    .size(32.dp)
                    .padding(6.dp)
            )
            Text(text = label)
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = count.toString(),
                color = MaterialTheme.colorScheme.onSurface.copy(0.5f)
            )
        }
    }

    @Composable
    private fun PinnedItems(
        pinned: ImmutableList<dev.materii.gloom.api.model.Pinnable?>
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text(
                stringResource(R.string.section_title_pinned),
                style = MaterialTheme.typography.labelLarge,
                fontSize = 15.sp,
                modifier = Modifier.padding(horizontal = 18.dp)
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(pinned) {
                    when (it) {
                        is ModelRepo -> {
                            RepoItem(it, card = true)
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun FollowButton(
        viewModel: ProfileViewModel,
        modifier: Modifier = Modifier
    ) {
        val (icon, alt) = if (viewModel.user?.isFollowing == true)
            Icons.Filled.HowToReg to stringResource(
                R.string.action_unfollow_user,
                viewModel.user?.username ?: "ghost"
            )
        else Icons.Outlined.PersonAddAlt to stringResource(
            R.string.action_follow_user,
            viewModel.user?.username ?: "ghost"
        )

        if (viewModel.user?.canFollow == true) {
            IconButton(
                onClick = { viewModel.toggleFollowing() },
                modifier = modifier
            ) {
                Icon(icon, contentDescription = alt)
            }
        }
    }

}