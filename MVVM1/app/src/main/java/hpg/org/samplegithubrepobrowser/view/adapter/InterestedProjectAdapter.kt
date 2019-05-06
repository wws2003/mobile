package hpg.org.samplegithubrepobrowser.view.adapter

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import hpg.org.samplegithubrepobrowser.R
import hpg.org.samplegithubrepobrowser.databinding.ListItemInterestedProjectBinding
import hpg.org.samplegithubrepobrowser.view.callback.ProjectClickCallback

class InterestedProjectAdapter(projectClickCallback: ProjectClickCallback?) :
    AbstractProjectAdapter(projectClickCallback) {

    @Override
    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        (holder.binding as ListItemInterestedProjectBinding).project = requireNotNull(getProjectList())[position]
        holder.binding.executePendingBindings()
    }

    @Override
    override fun getViewDataBinding(
        parent: ViewGroup,
        viewType: Int,
        projectClickCallback: ProjectClickCallback?
    ): ViewDataBinding {

        // Create binding based on layout resource file
        val binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.list_item_interested_project, parent,
            false
        ) as ListItemInterestedProjectBinding

        // Also set callback for binding (the callback is declared in layout file)
        binding.callback = projectClickCallback

        return binding
    }
}
