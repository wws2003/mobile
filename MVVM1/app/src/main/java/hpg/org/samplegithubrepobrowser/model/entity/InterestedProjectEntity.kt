package hpg.org.samplegithubrepobrowser.model.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.support.annotation.NonNull

@Entity(
    tableName = "TBL_INTERESTED_PROJECT",
    primaryKeys = ["USER_ID", "PROJECT_ID"],
    indices = [Index("USER_ID", "PROJECT_ID")]
)
data class InterestedProjectEntity(
    // Do not use ID of SQLite
    // Do not use local order in the first version of DB

    // Consider the primary keys consist of 2 columns: user_name and project_id

    @ColumnInfo(name = "USER_ID")
    @NonNull
    var userId: Long,

    @ColumnInfo(name = "PROJECT_ID")
    @NonNull
    var projectId: Long,

    @ColumnInfo(name = "PROJECT_NAME")
    var projectName: String?
)