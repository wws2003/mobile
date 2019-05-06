package hpg.org.samplegithubrepobrowser

import android.app.Application
import hpg.org.samplegithubrepobrowser.model.container.RepositoryContainer
import hpg.org.samplegithubrepobrowser.model.repository.db.AppDatabase

class MainApp : Application() {

    override fun onCreate() {
        super.onCreate()
        // Initialize DB
        AppDatabase.initialize(applicationContext)
        // Init services
        repositoryContainer.initialize()
    }

    companion object {

        val GITHUB_USER_NAME = "Tsutou"

        val GITHUB_USER_ID = 29670270L

        private var repositoryContainer: RepositoryContainer = RepositoryContainer()

        fun getRepositoryContainer(): RepositoryContainer {
            return repositoryContainer
        }
    }
}