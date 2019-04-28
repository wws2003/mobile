package hpg.org.samplegithubrepobrowser.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.databinding.ObservableField
import hpg.org.samplegithubrepobrowser.R
import hpg.org.samplegithubrepobrowser.service.model.Project
import hpg.org.samplegithubrepobrowser.service.repository.ProjectRepository

class ProjectViewModel(application: Application, mProjectID: String) : AndroidViewModel(application) {

    val observableProject: LiveData<Project> = ProjectRepository
        .instance
        .getProjectDetails(application.getString(R.string.github_user_name), mProjectID)

    var project = ObservableField<Project>()

    // Some setter
    fun setProject(proj: Project) {
        this.project.set(proj)
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