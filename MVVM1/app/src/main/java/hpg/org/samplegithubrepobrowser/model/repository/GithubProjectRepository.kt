package hpg.org.samplegithubrepobrowser.model.repository

import android.arch.lifecycle.LiveData
import hpg.org.samplegithubrepobrowser.model.dto.Project

/**
 * Data provider for ViewModel
 * Wrap response into LiveData object
 * Create service instance in constructor
 */
interface GithubProjectRepository {
    fun getProjectList(userId: String): LiveData<List<Project>>

    fun getProjectDetails(userId: String, projectName: String): LiveData<Project>
}