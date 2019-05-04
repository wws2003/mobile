package hpg.org.samplegithubrepobrowser.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.databinding.ObservableBoolean
import android.databinding.ObservableInt
import android.util.Log
import hpg.org.samplegithubrepobrowser.MainApp
import hpg.org.samplegithubrepobrowser.R
import hpg.org.samplegithubrepobrowser.model.dto.Project

/**
 * Response for binding data from repository of List<Project>
 * Perform data transformation if required (https://developer.android.com/topic /libraries/architecture/livedata.html#transformations_of_livedata
)
 */
class ProjectListViewModel(application: Application) : AndroidViewModel(application) {

    /**
     * Just a data holder
     */
    private var projectListObservable: LiveData<List<Project>> = MainApp.getRepositoryContainer()
        .getGithubProjectRepository()
        .getProjectList(getApplication<Application>().getString(R.string.github_user_name))

    private var isLoading = ObservableBoolean(true)

    private var projectCount = ObservableInt(0)

    private var currentProjectList: List<Project>? = null

    /**
     * Called from View to save interested project.
     * This system follows the approach
     */
    fun saveInterestedProjects(lifeCycleOwner: LifecycleOwner, projectListObserver: Observer<List<Project>>) {
        val projectsToSave = ArrayList<Project>()
        for (project in projectListObservable.value!!) {
            project.interested?.let {
                projectsToSave.add(project)
            }
        }

        val interestedProjectRepository = MainApp.getRepositoryContainer().getInterestedProjectRepository()
        interestedProjectRepository.saveInterestedProjects(projectsToSave)
            .observe(lifeCycleOwner, Observer { savedIds ->
                // Mark projects as interested
                for (prj in currentProjectList ?: ArrayList()) {
                    // TODO Handle the local order
                    // Perform Model observation
                    if (savedIds?.contains(prj.id) == true) {
                        prj.interested = true
                        prj.localOrder = 1
                        Log.d(LOG_TAG, "Saved id " + prj.localOrder + " " + prj.interested)
                    }
                }
                projectListObserver.onChanged(currentProjectList)
            })
    }

    /**
     * Started to be observed by the View
     */
    fun loadProjects(lifeCycleOwner: LifecycleOwner, projectListObserver: Observer<List<Project>>) {
        projectListObservable
            .observe(lifeCycleOwner, Observer { projects ->
                if (projects != null) {
                    logRetrievedProjects(projects)
                    // Also update other attribute
                    setIsLoading(false)
                    setProjectCount(projects.size)
                    currentProjectList = projects
                    // Notify observer
                    projectListObserver.onChanged(currentProjectList)
                }
            })
    }

    fun isLoading(): ObservableBoolean {
        return isLoading
    }

    fun projectCount(): ObservableInt {
        return projectCount
    }


    // Some setter
    private fun setProjectCount(prjCnt: Int) {
        this.projectCount.set(prjCnt)
    }

    private fun setIsLoading(loading: Boolean) {
        this.isLoading.set(loading)
    }

    private fun logRetrievedProjects(projects: List<Project>) {
        // Log for test
        Log.d(LOG_TAG, "Number of project retrieved: " + projects.size)
        for (project in projects) {
            Log.d(LOG_TAG, "Project: " + project.full_name + " " + project.owner?.id)
        }
    }

    companion object {
        val LOG_TAG: String = ProjectListViewModel::class.java.name
    }
}