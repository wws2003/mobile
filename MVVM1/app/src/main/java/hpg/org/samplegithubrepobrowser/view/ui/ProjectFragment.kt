package hpg.org.samplegithubrepobrowser.view.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import hpg.org.samplegithubrepobrowser.R
import hpg.org.samplegithubrepobrowser.databinding.FragmentProjectDetailsBinding
import hpg.org.samplegithubrepobrowser.viewmodel.ProjectViewModel
import java.util.*

private const val KEY_PROJECT_ID = "project_id"

class ProjectFragment : Fragment() {

    private var binding: FragmentProjectDetailsBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Initialize view with binding
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_project_details,
            container,
            false
        )
        return requireNotNull(binding).root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // Assign viewModel

        //DI to create instance with projectID parameter
        assert(arguments != null)
        val factory = ProjectViewModel.Factory(
            Objects.requireNonNull<FragmentActivity>(activity).application,
            requireNotNull(arguments).getString(KEY_PROJECT_ID)!!
        )

        val viewModel = ViewModelProviders.of(this, factory).get(ProjectViewModel::class.java)

        // Loading
        viewModel.setIsLoading(true)

        // Wire to view
        binding!!.projectViewModel = viewModel

        // Start to observe ViewModel
        observeViewModel(viewModel)
    }

    /**
     * Observe livedata bound to the ViewModel
     */
    private fun observeViewModel(viewModel: ProjectViewModel) {
        viewModel.observableProject.observe(this, Observer { project ->
            if (project != null) {
                // Set attribute here is just to update view actually
                viewModel.setIsLoading(false)
                viewModel.setProject(project)
            }
        })
    }

    companion object FFF {
        /**
         * Create new fragment with the parameter (projectID)
         */
        fun forProject(projectID: String): ProjectFragment {
            val fragment = ProjectFragment()
            val args = Bundle()

            args.putString(KEY_PROJECT_ID, projectID)
            fragment.arguments = args

            return fragment
        }
    }
}
