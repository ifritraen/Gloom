package dev.materii.gloom.domain.manager

import android.content.Context
import androidx.compose.runtime.*
import androidx.core.content.edit
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.cache.normalized.apolloStore
import dev.materii.gloom.util.Logger
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

class AuthManager(
    context: Context,
    logger: Logger,
    private val apollo: ApolloClient,
    private val json: Json
) {

    private val settings = context.getSharedPreferences("auth", Context.MODE_PRIVATE)

    // ID to account
    val accounts = mutableStateMapOf<String, Account>()

    init {
        for (key in settings.all.keys) {
            try {
                val acct = json.decodeFromString<Account>(settings.getString(key, "")!!)
                accounts[key] = acct
            } catch (e: SerializationException) {
                if (key != LOGGED_IN_ID) logger.error("AuthManager", "Error serializing account", e)
            }
        }
    }

    private var currentAccountId: String
        get() = settings.getString(LOGGED_IN_ID, "")!!
        set(value) = settings.edit { putString(LOGGED_IN_ID, value) }

    val currentAccount: Account?
        get() {
            if (!isSignedIn) return null
            return accounts[currentAccountId]
        }

    val authToken: String
        get() = currentAccount?.token ?: ""

    val isSignedIn: Boolean get() = currentAccountId.isNotBlank() && awaitingAuthType == null

    var awaitingAuthType: Account.Type? = null
        private set

    var loading by mutableStateOf(false)
        private set

    fun setAuthState(authType: Account.Type? = awaitingAuthType, loading: Boolean = this.loading) {
        awaitingAuthType = authType
        this.loading = loading
    }

    fun addAccount(
        id: String,
        token: String,
        type: Account.Type = Account.Type.REGULAR,
        baseUrl: String? = null,
        avatarUrl: String,
        username: String,
        displayName: String?,
        notificationCount: Int = 0
    ) {
        Account(id, token, type, baseUrl, avatarUrl, username, displayName, notificationCount).let {
            accounts[id] = it
            settings.edit {
                putString(id, json.encodeToString(it))
            }
        }
    }

    fun editAccount(
        id: String,
        token: String = accounts[id]!!.token,
        type: Account.Type = accounts[id]!!.type,
        baseUrl: String? = accounts[id]!!.baseUrl,
        avatarUrl: String = accounts[id]!!.avatarUrl,
        username: String = accounts[id]!!.username,
        displayName: String? = accounts[id]!!.displayName,
        notificationCount: Int = accounts[id]!!.notificationCount
    ) = editAccount(
        accounts[id]!!,
        token,
        type,
        baseUrl,
        avatarUrl,
        username,
        displayName,
        notificationCount
    )

    private fun editAccount(
        account: Account,
        token: String = account.token,
        type: Account.Type = account.type,
        baseUrl: String? = account.baseUrl,
        avatarUrl: String = account.avatarUrl,
        username: String = account.username,
        displayName: String? = account.displayName,
        notificationCount: Int = account.notificationCount
    ) {
        accounts[account.id]?.let {
            accounts[account.id] = account.copy(
                token = token,
                type = type,
                baseUrl = baseUrl,
                avatarUrl = avatarUrl,
                username = username,
                displayName = displayName,
                notificationCount = notificationCount
            )
            settings.edit {
                putString(it.id, json.encodeToString(it))
            }
        }
    }

    fun removeAccount(id: String) {
        if (id == currentAccountId) currentAccountId = ""
        accounts.remove(id)
        settings.edit {
            remove(id)
        }
    }

    fun switchToAccount(id: String) {
        if (accounts.containsKey(id)) currentAccountId = id
        clearApolloCache()
    }

    fun clearApolloCache() {
        apollo.apolloStore.clearAll()
    }

    companion object {

        const val LOGGED_IN_ID = "logged_in_id"

    }

}

@Stable
@Serializable
data class Account(
    val id: String,
    val token: String,
    val type: Type,
    val baseUrl: String?,
    val avatarUrl: String,
    val username: String,
    val displayName: String?,
    val notificationCount: Int = 0
) {

    @Serializable
    enum class Type {

        REGULAR,
        ENTERPRISE
    }

}