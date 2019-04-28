package hpg.org.samplegithubrepobrowser.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import hpg.org.samplegithubrepobrowser.R
import hpg.org.samplegithubrepobrowser.service.model.Project
import hpg.org.samplegithubrepobrowser.service.repository.ProjectRepository

/**
 * Response for binding data from repository of List<Project>
 * Perform data transformation if required (https://developer.android.com/topic /libraries/architecture/livedata.html#transformations_of_livedata
)
 */
class ProjectListViewModel(application: Application) : AndroidViewModel(application) {

    /**
     * Just a data holder
     */
    private var projectListObservable: LiveData<List<Project>> = ProjectRepository
        .instance
        .getProjectList(getApplication<Application>().getString(R.string.github_user_name))

    fun getProjectListObservable(): LiveData<List<Project>> {
        return projectListObservable
    }
}