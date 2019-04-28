package hpg.org.samplegithubrepobrowser.view.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import hpg.org.samplegithubrepobrowser.R
import hpg.org.samplegithubrepobrowser.service.model.Project

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            // Show fragment for project list
            val fragment = ProjectListFragment.forProjectList()

            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, fragment, ProjectListFragment.TAG_OF_PROJECT_LIST_FRAGMENT)
                .commit()
        }
    }

    // Show project
    fun show(project: Project) {
        val projectFragment = ProjectFragment.forProject(project.name)

        supportFragmentManager
            .beginTransaction()
            .addToBackStack("project")
            .replace(R.id.fragment_container, projectFragment, null)
            .commit()
    }

}
