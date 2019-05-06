package hpg.org.samplegithubrepobrowser.model.repository

import hpg.org.samplegithubrepobrowser.model.dto.Project

class InterestedProjectSessionImpl : InterestedProjectSession {

    private var sessionData: List<Project>? = null

    override fun remember(interestedProjects: List<Project>) {
        this.sessionData = interestedProjects
    }

    override fun load(): List<Project>? {
        return this.sessionData
    }
}