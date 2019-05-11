package hpg.org.samplegithubrepobrowser.viewmodel

import android.app.Application
import android.arch.lifecycle.*
import android.databinding.ObservableBoolean
import android.databinding.ObservableInt
import android.util.Log
import hpg.org.samplegithubrepobrowser.MainApp
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
    private var projectListObservable: LiveData<List<Project>> = getUserProjectLiveData(MainApp.GITHUB_USER_NAME)

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
                val interestedProjects: MutableList<Project> = ArrayList()
                // Mark projects as interested
                for (prj in currentProjectList ?: ArrayList()) {
                    // TODO Handle the local order properly
                    // Perform Model observation
                    if (savedIds?.contains(prj.id) == true) {
                        prj.interested = true
                        prj.localOrder = 1
                        interestedProjects.add(prj)
                    }
                }
                projectListObserver.onChanged(currentProjectList)
                // Also update session
                MainApp.getRepositoryContainer()
                    .getInterestedProjectSession()
                    .remember(interestedProjects)
            })
    }

    /**
     * Load projects for observation
     */
    fun loadProjectsForObservation(lifeCycleOwner: LifecycleOwner, projectListObserver: Observer<List<Project>>) {
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

    /*---------------------------------Private methods----------------------*/

    /**
     * Get livedata (observable stream) of user projects
     */
    private fun getUserProjectLiveData(userName: String): LiveData<List<Project>> {
        return Transformations.switchMap(
            MainApp.getRepositoryContainer()
                .getInterestedProjectRepository()
                .loadInterestedProjects()
        ) { interestedProjects ->

            // Update to session TODO Move else where to eliminate Side-effect !?
            MainApp.getRepositoryContainer()
                .getInterestedProjectSession()
                .remember(interestedProjects)

            // Livedata from Github repository
            val userProjectsLiveData: LiveData<List<Project>> = MainApp.getRepositoryContainer()
                .getGithubProjectRepository()
                .getProjectList(userName)

            // Mark interested projects
            Transformations.map(userProjectsLiveData) { userProjects ->
                val ret: MutableList<Project> = ArrayList<Project>()

                for (userPrj in userProjects) {
                    val prj = userPrj.copy(owner = userPrj.owner?.copy())
                    var interested = false
                    // Check if project is in interested list
                    for (interestedPrj in interestedProjects) {
                        if (interestedPrj.id == userPrj.id && interestedPrj.owner?.id == userPrj.owner?.id) {
                            interested = true;
                            break
                        }
                    }
                    // If interested project, set corresponding attributes
                    if (interested) {
                        prj.localOrder = 1
                        prj.interested = true
                    }
                    ret.add(prj)
                }
                ret.toList()
            }
        }
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