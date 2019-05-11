package hpg.org.samplegithubrepobrowser.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.databinding.ObservableBoolean
import android.databinding.ObservableInt
import hpg.org.samplegithubrepobrowser.MainApp
import hpg.org.samplegithubrepobrowser.model.dto.Project

class InterestedProjectListViewModel(app: Application) : AndroidViewModel(app) {

    private var interestedProjectListObservable: LiveData<List<Project>> = getObservableFromSession()

    private var isLoading = ObservableBoolean(true)

    private var projectCount = ObservableInt(0)

    /**
     * Load the interested projects for observation
     */
    fun loadInterestedProjectsForObservation(
        lifeCycleOwner: LifecycleOwner,
        projectListObserver: Observer<List<Project>>
    ) {
        interestedProjectListObservable.observe(lifeCycleOwner, Observer { projects ->
            if (projects != null) {
                setIsLoading(false)
                setProjectCount(projects.size)
                // Notify observer
                projectListObserver.onChanged(projects)
            }
        })
    }

    fun isLoading(): ObservableBoolean {
        return isLoading
    }

    fun projectCount(): ObservableInt {
        return projectCount
    }

    /*----------------------Private methods------------------*/

    private fun getObservableFromSession(): LiveData<List<Project>> {
        return MainApp.getRepositoryContainer()
            .getInterestedProjectSession()
            .loadLive()
    }

    // Some setter
    private fun setProjectCount(prjCnt: Int) {
        this.projectCount.set(prjCnt)
    }

    private fun setIsLoading(loading: Boolean) {
        this.isLoading.set(loading)
    }
}