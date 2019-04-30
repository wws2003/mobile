package hpg.org.samplegithubrepobrowser.model.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import hpg.org.samplegithubrepobrowser.model.dto.Project
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GithubProjectRepositoryImpl : GithubProjectRepository {
    // Data service
    private var githubService: GithubService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(HTTPS_API_GITHUB_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Do not need to have explicit implementation class
        githubService = retrofit.create(GithubService::class.java)
    }

    override fun getProjectList(userId: String): LiveData<List<Project>> {
        val data = MutableLiveData<List<Project>>()
        // Enqueue async request
        githubService.getProjectList(userId)
            .enqueue(object : Callback<List<Project>> {
                override fun onResponse(call: Call<List<Project>>, response: Response<List<Project>>?) {
                    if (response != null) {
                        // Set data from background thread
                        data.postValue(response.body())
                        Log.d("logs:", "getProjectList" + response.body().toString())
                    }
                }

                override fun onFailure(call: Call<List<Project>>, t: Throwable) {
                    data.postValue(null)
                    Log.d("logs:", "getProjectList:onFailure")
                }
            })
        return data
    }

    override fun getProjectDetails(userId: String, projectName: String): LiveData<Project> {
        val data = MutableLiveData<Project>()

        githubService.getProjectDetail(userId, projectName)
            .enqueue(object : Callback<Project> {
                override fun onResponse(call: Call<Project>, response: Response<Project>?) {
                    if (response != null) {
                        simulateDelay()
                        data.postValue(response.body())
                        Log.d("logs:", "getProjectDetails" + response.body().toString())
                    }
                }

                override fun onFailure(call: Call<Project>, t: Throwable) {
                    data.postValue(null)
                    Log.d("logs:", "getProjectDetails:onFailure")
                }
            })
        return data
    }

    private fun simulateDelay() {
        try {
            Thread.sleep(10)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

    }
}