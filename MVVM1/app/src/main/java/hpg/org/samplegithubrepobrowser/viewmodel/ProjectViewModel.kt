package hpg.org.samplegithubrepobrowser.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import hpg.org.samplegithubrepobrowser.R
import hpg.org.samplegithubrepobrowser.model.dto.Project
import hpg.org.samplegithubrepobrowser.model.repository.ProjectRemoteRepository

class ProjectViewModel(application: Application, projectName: String) : AndroidViewModel(application) {

    val observableProject: LiveData<Project> = ProjectRemoteRepository
        .instance
        .getProjectDetails(application.getString(R.string.github_user_name), projectName)

    var project = ObservableField<Project>()

    var isLoading = ObservableBoolean(true)

    // Some setter
    fun setProject(proj: Project) {
        this.project.set(proj)
    }

    fun setIsLoading(loading: Boolean) {
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