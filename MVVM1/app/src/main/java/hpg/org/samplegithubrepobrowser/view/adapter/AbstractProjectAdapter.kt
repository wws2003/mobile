package hpg.org.samplegithubrepobrowser.view.adapter

import android.databinding.ViewDataBinding
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import hpg.org.samplegithubrepobrowser.model.dto.Project
import hpg.org.samplegithubrepobrowser.view.callback.ProjectClickCallback

abstract class AbstractProjectAdapter(private val projectClickCallback: ProjectClickCallback?) :
    RecyclerView.Adapter<AbstractProjectAdapter.ProjectViewHolder>() {

    // Some models inside
    private var projectList: List<Project>? = null

    // Backed models to detect changes
    private var backedProjectList: List<Project>? = null

    /**
     * Apply difference in project list to recycle view
     * The params projects is used for 2-way binding so subjected to be mutated by the ViewModel
     */
    fun setProjectList(projects: List<Project>) {
        if (this.projectList == null) {
            this.projectList = projects
            this.backedProjectList = cloneProjectList(projects)
            // Notify (but whom ?)
            notifyItemRangeInserted(0, projects.size)
        } else {
            val diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun getOldListSize(): Int {
                    return requireNotNull(this@AbstractProjectAdapter.backedProjectList).size
                }

                override fun getNewListSize(): Int {
                    return projects.size
                }

                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    return requireNotNull(this@AbstractProjectAdapter.backedProjectList)[oldItemPosition].id == projects[newItemPosition].id
                }

                override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    val project = projects[newItemPosition]
                    val old = requireNotNull(this@AbstractProjectAdapter.backedProjectList)[oldItemPosition]

                    return project.id == old.id
                            && project.git_url == old.git_url
                            && project.interested == old.interested
                            && project.localOrder == old.localOrder
                }
            })

            this.projectList = projects
            this.backedProjectList = cloneProjectList(projects)

            // Apply difference to recycle view
            diff.dispatchUpdatesTo(this)
        }
    }

    /**
     * Allow sub-classes to access
     */
    protected fun getProjectList(): List<Project>? {
        return this.projectList
    }

    @Override
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractProjectAdapter.ProjectViewHolder {
        val binding = getViewDataBinding(parent, viewType, this.projectClickCallback)
        return AbstractProjectAdapter.ProjectViewHolder(binding)
    }

    /**
     * Get the size of items in list
     */
    @Override
    override fun getItemCount(): Int {
        return if (projectList == null) 0 else projectList!!.size
    }

    private fun cloneProjectList(projectList: List<Project>): List<Project> {
        val clonedProjects = ArrayList<Project>()
        for (project in projectList) {
            clonedProjects.add(project.copy(owner = project.owner?.copy()))
        }
        return clonedProjects
    }

    /**
     * Get the view databinding for one project item
     */
    abstract fun getViewDataBinding(
        parent: ViewGroup,
        viewType: Int,
        projectClickCallback: ProjectClickCallback?
    ): ViewDataBinding

    /**
     * Extends of the RecyclerView.ViewHolder class, to bind to list_item_project view
     */
    open class ProjectViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root)
}