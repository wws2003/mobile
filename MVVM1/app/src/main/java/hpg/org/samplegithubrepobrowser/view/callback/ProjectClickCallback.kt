package hpg.org.samplegithubrepobrowser.view.callback

import hpg.org.samplegithubrepobrowser.model.dto.Project

/**
 * Interface for the action of clicking on a project in the list
 */
interface ProjectClickCallback {
    /**
     * Called after click on the given project
     */
    fun onClick(project: Project)
}