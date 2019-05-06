package hpg.org.samplegithubrepobrowser.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import hpg.org.samplegithubrepobrowser.MainApp
import hpg.org.samplegithubrepobrowser.model.dto.Project

class InterestedProjectListViewModel(app: Application) : AndroidViewModel(app) {

    
    fun getInterestedProjectListObservable(): LiveData<List<Project>> {

        val interestedProjectListLiveData: MutableLiveData<List<Project>> = MutableLiveData()

        MainApp.getRepositoryContainer()
            .getInterestedProjectSession()
            .load()
            ?.let {
                interestedProjectListLiveData.postValue(it)
            }
        return interestedProjectListLiveData
    }
}