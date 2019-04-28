package hpg.org.samplegithubrepobrowser.view.adapter

import android.databinding.DataBindingUtil
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import hpg.org.samplegithubrepobrowser.R
import hpg.org.samplegithubrepobrowser.databinding.ProjectListItemBinding
import hpg.org.samplegithubrepobrowser.service.model.Project
import hpg.org.samplegithubrepobrowser.view.callback.ProjectClickCallback

class ProjectAdapter(private val projectClickCallback: ProjectClickCallback?) :
    RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder>() {

    // Some models inside
    private var projectList: List<Project>? = null

    /**
     * Apply difference in project list to recycle view
     */
    fun setProjectList(projectList: List<Project>) {
        if (this.projectList == null) {
            this.projectList = projectList
            // Notify (but whom ?)
            notifyItemRangeInserted(0, projectList.size)
        } else {
            val diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun getOldListSize(): Int {
                    return requireNotNull(this@ProjectAdapter.projectList).size
                }

                override fun getNewListSize(): Int {
                    return projectList.size
                }

                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    return requireNotNull(this@ProjectAdapter.projectList)[oldItemPosition].id == projectList[newItemPosition].id
                }

                override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    val project = projectList[newItemPosition]
                    val old = projectList[oldItemPosition]

                    return project.id == old.id && project.git_url == old.git_url
                }
            })

            this.projectList = projectList

            // Apply difference to recycle view
            diff.dispatchUpdatesTo(this)
        }
    }

    @Override
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
        val binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.project_list_item, parent,
            false
        ) as ProjectListItemBinding

        // Also set callback for binding (the callback is declared in layout file)
        binding.callback = projectClickCallback

        return ProjectViewHolder(binding)
    }

    /**
     * Get the size of items in list
     */
    @Override
    override fun getItemCount(): Int {
        return if (projectList == null) 0 else projectList!!.size
    }

    @Override
    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        holder.binding.project = requireNotNull(projectList)[position]
        holder.binding.executePendingBindings()
    }

    /**
     * Extends of the RecyclerView.ViewHolder class, to bind to project_list_item view
     */
    open class ProjectViewHolder(val binding: ProjectListItemBinding) : RecyclerView.ViewHolder(binding.root)
}