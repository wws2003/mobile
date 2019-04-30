package hpg.org.samplegithubrepobrowser.model.repository

import android.arch.lifecycle.LiveData
import hpg.org.samplegithubrepobrowser.model.dto.Project

interface InterestedProjectRepository {
    /**
     * Save project into local persistence storage
     * Return the id of project saved
     */
    fun saveInterestedProject(project: Project): LiveData<Long>

    /**
     * Load saved projects of one user
     */
    fun loadInterestedProjects(userName: String): LiveData<List<Project>>
}