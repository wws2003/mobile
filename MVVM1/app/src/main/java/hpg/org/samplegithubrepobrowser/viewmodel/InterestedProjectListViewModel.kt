package hpg.org.samplegithubrepobrowser.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import hpg.org.samplegithubrepobrowser.model.dto.Project

class InterestedProjectListViewModel(app: Application, userName: String) : AndroidViewModel(app) {

    private var interestedProjectListObservable: LiveData<List<Project>>? = null

    fun getInterestedProjectListObservable(): LiveData<List<Project>> {
        return interestedProjectListObservable!!
    }
}