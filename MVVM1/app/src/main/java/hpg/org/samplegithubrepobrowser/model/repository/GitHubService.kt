package hpg.org.samplegithubrepobrowser.model.repository

import hpg.org.samplegithubrepobrowser.model.dto.Project
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

// Retrofit URL
const val HTTPS_API_GITHUB_URL = "https://api.github.com/";

internal interface GitHubService {

    @GET("users/{user}/repos")
    fun getProjectList(@Path("user") user: String): Call<List<Project>>

    @GET("/repos/{user}/{reponame}")
    fun getProjectDetail(@Path("user") user: String, @Path("reponame") projectName: String): Call<Project>
}