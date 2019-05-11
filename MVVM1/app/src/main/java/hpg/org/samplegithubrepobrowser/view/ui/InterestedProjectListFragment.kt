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
import hpg.org.samplegithubrepobrowser.databinding.FragmentInterestedProjectListBinding
import hpg.org.samplegithubrepobrowser.model.dto.Project
import hpg.org.samplegithubrepobrowser.view.adapter.InterestedProjectAdapter
import hpg.org.samplegithubrepobrowser.view.callback.BackPressedListener
import hpg.org.samplegithubrepobrowser.view.callback.ProjectClickCallback
import hpg.org.samplegithubrepobrowser.viewmodel.InterestedProjectListViewModel

class InterestedProjectListFragment : Fragment(), ProjectClickCallback, BackPressedListener {

    private var interestedProjectAdapter: InterestedProjectAdapter? = null

    private var binding: FragmentInterestedProjectListBinding? = null

    private var viewModel: InterestedProjectListViewModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate view
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_interested_project_list,
            container,
            false
        )

        // Initialize adapter with callback as this
        interestedProjectAdapter = InterestedProjectAdapter(this)

        // Bind adapter to view
        binding!!.projectList.adapter = interestedProjectAdapter

        // Init values
        return binding!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // Assign viewModel for 2-way binding
        viewModel = ViewModelProviders.of(this).get(InterestedProjectListViewModel::class.java)
        binding?.viewModel = viewModel
        // Start to observe ViewModel
        viewModel?.loadInterestedProjectsForObservation(this, getObserverForAdapter())
    }

    override fun onClick(project: Project) {
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED) && activity is MainActivity) {
            // Show project detail from MainActivity
            // (activity as MainActivity).show(project)
            childFragmentManager.beginTransaction()
                .addToBackStack("project")
                .add(R.id.fl_project_list, ProjectFragment.forProject(project.owner?.login!!, project.name), "project")
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

    /*----------------------------Private methods---------------------------*/

    /**
     * Get the projects list adapter just to fill the adapter
     */
    private fun getObserverForAdapter(): Observer<List<Project>> {
        return Observer { interestedProjects ->
            interestedProjects?.let { prjs ->
                Log.d(TAG, "Interested projects count: " + prjs.size)
                interestedProjectAdapter?.setProjectList(prjs)
            }
        }
    }

    companion object {

        val TAG = InterestedProjectListFragment::class.java.name

        /**
         * Instantiate a new fragment
         */
        fun forInterestedProjectListFragment(): InterestedProjectListFragment {
            return InterestedProjectListFragment()
        }
    }
}