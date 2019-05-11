package hpg.org.samplegithubrepobrowser.model.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import hpg.org.samplegithubrepobrowser.model.dto.Project

class InterestedProjectSessionImpl : InterestedProjectSession {

    private var sessionData: List<Project>? = null

    private var sessionObservable: MutableLiveData<List<Project>> = MutableLiveData()

    override fun remember(interestedProjects: List<Project>) {
        this.sessionData = interestedProjects
        sessionObservable.postValue(interestedProjects)
    }

    override fun load(): List<Project>? {
        return this.sessionData
    }

    override fun loadLive(): LiveData<List<Project>> {
        return sessionObservable
    }
}