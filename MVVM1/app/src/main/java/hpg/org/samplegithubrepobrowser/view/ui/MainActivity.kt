package hpg.org.samplegithubrepobrowser.view.ui

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import hpg.org.samplegithubrepobrowser.R
import hpg.org.samplegithubrepobrowser.view.adapter.ViewPagerAdapter
import hpg.org.samplegithubrepobrowser.view.callback.BackPressedListener

class MainActivity : AppCompatActivity() {

    private var pagerAdapter: ViewPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val pager = findViewById<ViewPager>(R.id.pager)
        pagerAdapter = ViewPagerAdapter(supportFragmentManager, this)

        pager.adapter = pagerAdapter
    }

    override fun onBackPressed() {
        var eventConsumed = false
        for (fragment in supportFragmentManager.fragments) {
            if (fragment is BackPressedListener && fragment.onBackPressed()) {
                eventConsumed = true
                break
            }
        }
        if (!eventConsumed) {
            super.onBackPressed()
        }
    }
}
