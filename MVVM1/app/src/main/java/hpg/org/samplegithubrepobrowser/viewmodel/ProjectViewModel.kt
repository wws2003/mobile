package hpg.org.samplegithubrepobrowser.viewmodel

import android.app.Application
import android.arch.lifecycle.*
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import hpg.org.samplegithubrepobrowser.MainApp
import hpg.org.samplegithubrepobrowser.R
import hpg.org.samplegithubrepobrowser.model.dto.Project

class ProjectViewModel(application: Application, projectName: String) : AndroidViewModel(application) {

    private val observableProject: LiveData<Project> = MainApp.getRepositoryContainer()
        .getGithubProjectRepository()
        .getProjectDetails(application.getString(R.string.github_user_name), projectName)

    private var project = ObservableField<Project>()

    private var isLoading = ObservableBoolean(true)

    fun loadProject(lifeCycleOwner: LifecycleOwner, projectListObserver: Observer<Project>) {
        observableProject
            .observe(lifeCycleOwner, Observer { project ->
                if (project != null) {
                    // Set attribute here is just to update view actually
                    setIsLoading(false)
                    setProject(project)
                    // Callback
                    projectListObserver.onChanged(project)
                }
            })
    }

    fun isLoading(): ObservableBoolean {
        return isLoading
    }

    fun project(): ObservableField<Project> {
        return project
    }

    // Some setter
    private fun setProject(proj: Project) {
        this.project.set(proj)
    }

    private fun setIsLoading(loading: Boolean) {
        this.isLoading.set(loading)
    }

    /**
     * Factory to create instance
     */
    class Factory(private val application: Application, private val projectID: String) :
        ViewModelProvider.NewInstanceFactory() {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {

            return ProjectViewModel(application, projectID) as T
        }
    }
}