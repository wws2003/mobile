package hpg.org.samplegithubrepobrowser.view.adapter

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import hpg.org.samplegithubrepobrowser.R
import hpg.org.samplegithubrepobrowser.view.ui.InterestedProjectListFragment
import hpg.org.samplegithubrepobrowser.view.ui.ProjectListFragment

class ViewPagerAdapter(fragmentManager: FragmentManager, context: Context) :
    FragmentPagerAdapter(fragmentManager) {

    private val tabNameMap: MutableMap<Int, String> = HashMap()

    private var selectedProjectName: String? = null

    init {
        tabNameMap[0] = context.getString(R.string.title_project_list)
        tabNameMap[1] = context.getString(R.string.title_interested_project_list)
    }

    fun setSelectedProjectId(projectName: String?) {
        this.selectedProjectName = projectName
        notifyDataSetChanged()
    }

    /**
     * Return the fragment corresponding to the page (tab) index
     */
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> ProjectListFragment.forProjectList()
            1 -> InterestedProjectListFragment.forInterestedProjectListFragment()
            else -> ProjectListFragment.forProjectList()
        }
    }

    /**
     * Return the title of the page (tab)
     */
    override fun getPageTitle(position: Int): CharSequence? {
        return tabNameMap.get(key = position)
    }

    /**
     * Return the number of pages (tabs)
     */
    override fun getCount(): Int {
        return 2
    }
}