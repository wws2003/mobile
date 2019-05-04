package hpg.org.samplegithubrepobrowser.model.repository.db

import android.arch.persistence.room.*
import hpg.org.samplegithubrepobrowser.model.entity.InterestedProjectEntity

@Dao
interface InterestedProjectDao {

    @Query("SELECT * FROM TBL_INTERESTED_PROJECT ORDER BY USER_ID, PROJECT_ID")
    fun selectAll(): List<InterestedProjectEntity>

    @Query("SELECT * FROM TBL_INTERESTED_PROJECT WHERE USER_ID=:userId AND PROJECT_ID IN (:projectIds) ORDER BY USER_ID, PROJECT_ID")
    fun selectIn(userId: Long, projectIds: List<Long>): List<InterestedProjectEntity>

    @Query("SELECT * FROM TBL_INTERESTED_PROJECT WHERE USER_ID=:userId ORDER BY USER_ID, PROJECT_ID")
    fun selectByUser(userId: Long): List<InterestedProjectEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: InterestedProjectEntity): Long

    @Update
    fun update(entity: InterestedProjectEntity): Int

    @Delete
    fun delete(entity: InterestedProjectEntity): Int
}