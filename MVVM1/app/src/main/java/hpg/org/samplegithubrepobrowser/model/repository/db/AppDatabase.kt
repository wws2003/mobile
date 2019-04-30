package hpg.org.samplegithubrepobrowser.model.repository.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import hpg.org.samplegithubrepobrowser.model.entity.InterestedProjectEntity

@Database(entities = [InterestedProjectEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getInterestedProjectDao(): InterestedProjectDao

    companion object {
        private var instance: AppDatabase? = null

        fun initialize(applicationContext: Context) {
            // Test memory DB first
            instance = Room.inMemoryDatabaseBuilder(
                applicationContext,
                AppDatabase::class.java
            ).build()
        }

        fun getInstance(): AppDatabase {
            return instance!!
        }
    }
}