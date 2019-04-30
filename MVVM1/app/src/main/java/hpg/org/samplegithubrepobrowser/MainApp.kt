package hpg.org.samplegithubrepobrowser

import android.app.Application
import hpg.org.samplegithubrepobrowser.model.repository.db.AppDatabase

class MainApp : Application() {

    override fun onCreate() {
        super.onCreate()
        // Initialize DB
        AppDatabase.initialize(applicationContext)
    }
}