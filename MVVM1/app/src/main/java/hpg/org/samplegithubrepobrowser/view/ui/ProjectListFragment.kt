package hpg.org.samplegithubrepobrowser.view.ui

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import hpg.org.samplegithubrepobrowser.R
import hpg.org.samplegithubrepobrowser.databinding.FragmentProjectListBinding
import hpg.org.samplegithubrepobrowser.model.dto.Project
import hpg.org.samplegithubrepobrowser.view.adapter.ProjectAdapter
import hpg.org.samplegithubrepobrowser.view.callback.BackPressedListener
import hpg.org.samplegithubrepobrowser.view.callback.InterestClickCallback
import hpg.org.samplegithubrepobrowser.view.callback.ProjectClickCallback
import hpg.org.samplegithubrepobrowser.viewmodel.ProjectListViewModel

class ProjectListFragment : Fragment(), ProjectClickCallback, BackPressedListener, InterestClickCallback {

    private var projectAdapter: ProjectAdapter? = null

    private var binding: FragmentProjectListBinding? = null

    private var viewModel: ProjectListViewModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate view
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_project_list, container, false)

        // Initialize adapter with callback as this
        projectAdapter = ProjectAdapter(this)

        // Bind adapter to view
        binding!!.projectList.adapter = projectAdapter
        binding!!.callback = this

        // Return view
        return requireNotNull(binding).root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // Assign viewModel
        viewModel = ViewModelProviders.of(this).get(ProjectListViewModel::class.java)
        binding?.viewModel = viewModel
        // Start to observe ViewModel
        viewModel?.loadProjects(this, getObserverForAdapter())
    }

    override fun onClick(project: Project) {
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED) && activity is MainActivity) {
            // Show project detail from MainActivity
            // (activity as MainActivity).show(project)
            childFragmentManager.beginTransaction()
                .addToBackStack("project")
                .add(R.id.fl_project_list, ProjectFragment.forProject(project.name), "project")
                .commit()
        }
    }

    override fun onBackPressed(): Boolean {
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED) && activity is MainActivity) {
            // Pop the project detail fragment
            if (childFragmentManager.backStackEntryCount > 0) {
                childFragmentManager.popBackStack()
                return true
            }
        }
        return false
    }

    override fun onBtnInterestClicked() {
        viewModel?.saveInterestedProjects(this, getObserverForAdapter())
    }

    /**
     * Get the projects list adapter just to fill the adapter
     */
    private fun getObserverForAdapter(): Observer<List<Project>> {
        return Observer { interestedProjects ->
            interestedProjects?.let { prjs ->
                projectAdapter?.setProjectList(prjs)
            }
        }
    }

    companion object {
        /**
         * New fragment instance
         */
        fun forProjectList(): ProjectListFragment {
            return ProjectListFragment()
        }
    }
}