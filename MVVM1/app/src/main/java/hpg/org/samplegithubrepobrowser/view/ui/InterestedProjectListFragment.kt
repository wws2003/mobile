package hpg.org.samplegithubrepobrowser.view.ui

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import hpg.org.samplegithubrepobrowser.R
import hpg.org.samplegithubrepobrowser.databinding.FragmentInterestedProjectListBinding
import hpg.org.samplegithubrepobrowser.viewmodel.InterestedProjectListViewModel

class InterestedProjectListFragment : Fragment() {

    private var binding: FragmentInterestedProjectListBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate view
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_interested_project_list,
            container,
            false
        )
        // Init values
        binding!!.isLoading = false
        return binding!!.root
    }

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