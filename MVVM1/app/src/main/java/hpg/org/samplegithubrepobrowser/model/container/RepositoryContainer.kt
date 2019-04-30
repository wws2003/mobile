package hpg.org.samplegithubrepobrowser.model.container

import hpg.org.samplegithubrepobrowser.model.repository.GithubProjectRepository
import hpg.org.samplegithubrepobrowser.model.repository.GithubProjectRepositoryImpl
import hpg.org.samplegithubrepobrowser.model.repository.InterestedProjectRepository
import hpg.org.samplegithubrepobrowser.model.repository.InterestedProjectRepositoryImpl
import hpg.org.samplegithubrepobrowser.model.repository.db.AppDatabase

class RepositoryContainer {

    private var githubProjectRepository: GithubProjectRepository? = null
    private var interestedProjectRepository: InterestedProjectRepository? = null

    @Synchronized
    fun initialize() {
        val database = AppDatabase.getInstance()
        githubProjectRepository = GithubProjectRepositoryImpl()
        interestedProjectRepository = InterestedProjectRepositoryImpl(database.getInterestedProjectDao())
    }

    @Synchronized
    fun getInterestedProjectRepository(): InterestedProjectRepository {
        return interestedProjectRepository!!
    }

    @Synchronized
    fun getGithubProjectRepository(): GithubProjectRepository {
        return githubProjectRepository!!
    }
}