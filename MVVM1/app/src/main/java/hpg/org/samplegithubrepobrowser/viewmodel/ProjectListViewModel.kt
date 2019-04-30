package hpg.org.samplegithubrepobrowser.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.databinding.ObservableBoolean
import android.databinding.ObservableInt
import hpg.org.samplegithubrepobrowser.R
import hpg.org.samplegithubrepobrowser.model.dto.Project
import hpg.org.samplegithubrepobrowser.model.repository.ProjectRemoteRepository

/**
 * Response for binding data from repository of List<Project>
 * Perform data transformation if required (https://developer.android.com/topic /libraries/architecture/livedata.html#transformations_of_livedata
)
 */
class ProjectListViewModel(application: Application) : AndroidViewModel(application) {

    /**
     * Just a data holder
     */
    private var projectListObservable: LiveData<List<Project>> = ProjectRemoteRepository
        .instance
        .getProjectList(getApplication<Application>().getString(R.string.github_user_name))

    var isLoading = ObservableBoolean(true)

    var projectCount = ObservableInt(0)

    fun getProjectListObservable(): LiveData<List<Project>> {
        return projectListObservable
    }

    // Some setter
    fun setProjectCount(prjCnt: Int) {
        this.projectCount.set(prjCnt)
    }

    fun setIsLoading(loading: Boolean) {
        this.isLoading.set(loading)
    }
}