package hpg.org.samplegithubrepobrowser.model.repository

import android.arch.lifecycle.LiveData
import hpg.org.samplegithubrepobrowser.model.dto.Project

interface InterestedProjectSession {

    fun remember(interestedProjects: List<Project>)

    fun load(): List<Project>?

    fun loadLive(): LiveData<List<Project>>
}