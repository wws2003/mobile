package hpg.org.samplegithubrepobrowser.model.repository.db

import android.arch.persistence.room.*
import hpg.org.samplegithubrepobrowser.model.entity.InterestedProjectEntity

@Dao
interface InterestedProjectDao {

    @Query("SELECT * FROM TBL_INTERESTED_PROJECT")
    fun selectAll(): List<InterestedProjectEntity>

    @Query("SELECT * FROM TBL_INTERESTED_PROJECT WHERE USER_NAME=:userName AND PROJECT_ID IN (:projectIds)")
    fun selectIn(userName: String, projectIds: List<Long>): List<InterestedProjectEntity>

    @Query("SELECT * FROM TBL_INTERESTED_PROJECT WHERE USER_NAME=:userName")
    fun selectByUser(userName: String): List<InterestedProjectEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: InterestedProjectEntity): Long

    @Update
    fun update(entity: InterestedProjectEntity): Int

    @Delete
    fun delete(entity: InterestedProjectEntity): Int
}