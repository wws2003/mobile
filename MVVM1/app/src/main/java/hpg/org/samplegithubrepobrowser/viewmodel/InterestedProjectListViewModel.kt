package hpg.org.samplegithubrepobrowser.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import hpg.org.samplegithubrepobrowser.MainApp
import hpg.org.samplegithubrepobrowser.model.dto.Project

class InterestedProjectListViewModel(app: Application) : AndroidViewModel(app) {

    private var interestedProjectListObservable: LiveData<List<Project>>? = MainApp.getRepositoryContainer()
        .getInterestedProjectRepository()
        .loadInterestedProjects()

    fun getInterestedProjectListObservable(): LiveData<List<Project>> {
        return interestedProjectListObservable!!
    }
}