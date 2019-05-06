package hpg.org.samplegithubrepobrowser.model.repository

import hpg.org.samplegithubrepobrowser.model.dto.Project

interface InterestedProjectSession {

    fun remember(interestedProjects: List<Project>)

    fun load(): List<Project>?
}