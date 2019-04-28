package hpg.org.samplegithubrepobrowser.service.model

import java.util.*

data class User(
    val login: String?,
    val id: Long,
    val avatar_url: String?,
    var gravatar_id: String?,
    var url: String?,
    var html_url: String?,
    var followers_url: String?,
    var following_url: String?,
    var gists_url: String?,
    var starred_url: String?,
    var subscriptions_url: String?,
    var organizations_url: String?,
    var repos_url: String?,
    var events_url: String?,
    var received_events_url: String?,
    var type: String?,
    var name: String?,
    var blog: String?,
    var location: String?,
    var email: String?,
    var public_repos: Int,
    var public_gists: Int,
    var followers: Int,
    var following: Int,
    var created_at: Date?,
    var updated_at: Date?
)