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

private const val KEY_USER_NAME = "user_name"

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
        val viewModel = getViewModel()

        // Wire to view
        binding!!.projectViewModel = viewModel

        // Start to observe ViewModel
        viewModel.loadProject(this, Observer { project ->
            // Do nothing at all for now ?
        })
    }

    /**
     * Get the ViewModel instance
     */
    private fun getViewModel(): ProjectViewModel {
        //DI to create instance with projectID parameter
        assert(arguments != null)
        val factory = ProjectViewModel.Factory(
            Objects.requireNonNull<FragmentActivity>(activity).application,
            requireNotNull(arguments).getString(KEY_USER_NAME)!!,
            requireNotNull(arguments).getString(KEY_PROJECT_ID)!!
        )

        return ViewModelProviders.of(this, factory).get(ProjectViewModel::class.java)
    }

    companion object FFF {
        /**
         * Create new fragment with the parameter (projectID)
         */
        fun forProject(userName: String, projectID: String): ProjectFragment {
            val fragment = ProjectFragment()
            val args = Bundle()

            args.putString(KEY_USER_NAME, userName)
            args.putString(KEY_PROJECT_ID, projectID)
            fragment.arguments = args

            return fragment
        }
    }
}
