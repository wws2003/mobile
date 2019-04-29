package hpg.org.samplegithubrepobrowser.model.repository

import android.arch.lifecycle.LiveData
import hpg.org.samplegithubrepobrowser.model.dto.Project

internal interface ProjectLocalRepository {
    /**
     * Save project into local persistence storage
     * Return the number of project saved
     */
    fun saveProject(project: Project): LiveData<Int>

    /**
     * Load saved projects of one user
     */
    fun loadProjects(userName: String): LiveData<List<Project>>
}