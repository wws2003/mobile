package hpg.org.samplegithubrepobrowser.model.repository

import android.arch.lifecycle.LiveData
import hpg.org.samplegithubrepobrowser.model.dto.Project

interface InterestedProjectRepository {
    /**
     * Save project into local persistence storage
     * Return the ids of projects saved
     */
    fun saveInterestedProjects(projects: List<Project>): LiveData<List<Long>>

    /**
     * Load all saved projects
     */
    fun loadInterestedProjects(): LiveData<List<Project>>

    /**
     * Load project by userId
     */
    fun loadInterestedProjects(userId: Long): LiveData<List<Project>>
}