package hpg.org.samplegithubrepobrowser.model.container

import hpg.org.samplegithubrepobrowser.model.repository.*
import hpg.org.samplegithubrepobrowser.model.repository.db.AppDatabase

class RepositoryContainer {

    private var githubProjectRepository: GithubProjectRepository? = null
    private var interestedProjectRepository: InterestedProjectRepository? = null
    private var interestedProjectSession: InterestedProjectSession? = null

    @Synchronized
    fun initialize() {
        val database = AppDatabase.getInstance()
        githubProjectRepository = GithubProjectRepositoryImpl()
        interestedProjectRepository = InterestedProjectRepositoryImpl(database.getInterestedProjectDao())
        interestedProjectSession = InterestedProjectSessionImpl()
    }

    @Synchronized
    fun getInterestedProjectRepository(): InterestedProjectRepository {
        return interestedProjectRepository!!
    }

    @Synchronized
    fun getGithubProjectRepository(): GithubProjectRepository {
        return githubProjectRepository!!
    }

    @Synchronized
    fun getInterestedProjectSession(): InterestedProjectSession {
        return interestedProjectSession!!
    }
}