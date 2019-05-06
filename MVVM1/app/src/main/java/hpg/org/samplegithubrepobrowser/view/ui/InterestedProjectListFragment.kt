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
import hpg.org.samplegithubrepobrowser.databinding.FragmentInterestedProjectListBinding
import hpg.org.samplegithubrepobrowser.model.dto.Project
import hpg.org.samplegithubrepobrowser.view.adapter.InterestedProjectAdapter
import hpg.org.samplegithubrepobrowser.view.callback.ProjectClickCallback
import hpg.org.samplegithubrepobrowser.viewmodel.InterestedProjectListViewModel

class InterestedProjectListFragment : Fragment(), ProjectClickCallback {

    private var interestedProjectAdapter: InterestedProjectAdapter? = null

    private var binding: FragmentInterestedProjectListBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate view
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_interested_project_list,
            container,
            false
        )

        // Initialize adapter with callback as this
        interestedProjectAdapter = InterestedProjectAdapter(this)

        // Init values
        binding!!.isLoading = true
        return binding!!.root
    }

    override fun onResume() {
        // Does not work since called only once
        // Renew data at resumed timing -> Not the problem of live data at all !
        super.onResume()

        val viewModel = ViewModelProviders.of(this).get(InterestedProjectListViewModel::class.java)
        viewModel.getInterestedProjectListObservable()
            .observe(this, Observer {
                it?.let { prjs ->
                    interestedProjectAdapter?.setProjectList(prjs)
                    binding!!.isLoading = false
                }
            })
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

    /*----------------------------Private methods---------------------------*/

    private fun observeViewModel(viewModel: InterestedProjectListViewModel) {
        // TODO Implement
    }


    companion object {
        /**
         * Instantiate a new fragment
         */
        fun forInterestedProjectListFragment(): InterestedProjectListFragment {
            return InterestedProjectListFragment()
        }
    }
}