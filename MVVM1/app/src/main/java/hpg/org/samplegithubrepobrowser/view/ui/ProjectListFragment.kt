package hpg.org.samplegithubrepobrowser.view.ui

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import hpg.org.samplegithubrepobrowser.R
import hpg.org.samplegithubrepobrowser.databinding.FragmentProjectListBinding
import hpg.org.samplegithubrepobrowser.model.dto.Project
import hpg.org.samplegithubrepobrowser.view.adapter.ProjectAdapter
import hpg.org.samplegithubrepobrowser.view.callback.BackPressedListener
import hpg.org.samplegithubrepobrowser.view.callback.ProjectClickCallback
import hpg.org.samplegithubrepobrowser.viewmodel.ProjectListViewModel

class ProjectListFragment : Fragment(), ProjectClickCallback, BackPressedListener {

    private var projectAdapter: ProjectAdapter? = null

    private var binding: FragmentProjectListBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate view
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_project_list, container, false)

        // Initialize adapter with callback as this
        projectAdapter = ProjectAdapter(this)

        // Bind adapter to view
        requireNotNull(binding).projectList.adapter = projectAdapter
        // Initialize isLoading as true
        requireNotNull(binding).isLoading = true
        requireNotNull(binding).projectCount = 0

        // Return view
        return requireNotNull(binding).root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // Assign viewModel
        val viewModel = ViewModelProviders.of(this).get(ProjectListViewModel::class.java)
        // Start to observe ViewModel
        observeViewModel(viewModel)
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

    private fun observeViewModel(projectListViewModel: ProjectListViewModel) {
        // Link to the LifeCycleOwner, add observer
        // Observer only receive event in the state of STARTED or RESUMED
        projectListViewModel.getProjectListObservable()
            .observe(this, Observer { projects ->
                if (projects != null) {
                    // Log for test
                    Log.d(LOG_TAG, "Number of project retrieved: " + projects.size)
                    for (project in projects) {
                        Log.d(LOG_TAG, "Project: " + project.full_name)
                    }
                    // Turn-off loading and set data to adapter
                    binding!!.isLoading = false
                    binding!!.projectCount = projects.size
                    projectAdapter!!.setProjectList(projects)
                }
            })
    }

    companion object {
        // Serve as const static field
        // val TAG_OF_PROJECT_LIST_FRAGMENT = "ProjectListFragment"

        val LOG_TAG: String = ProjectListFragment::class.java.name

        /**
         * New fragment instance
         */
        fun forProjectList(): ProjectListFragment {
            return ProjectListFragment()
        }
    }
}