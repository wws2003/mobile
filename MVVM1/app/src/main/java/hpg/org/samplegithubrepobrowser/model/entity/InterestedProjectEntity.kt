package hpg.org.samplegithubrepobrowser.model.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.support.annotation.NonNull

@Entity(
    tableName = "TBL_INTERESTED_PROJECT",
    primaryKeys = ["USER_NAME", "PROJECT_ID"],
    indices = [Index("USER_NAME", "PROJECT_ID")]
)
data class InterestedProjectEntity(
    // Do not use ID of SQLite
    // Do not use local order in the first version of DB

    // Consider the primary keys consist of 2 columns: user_name and project_id

    @ColumnInfo(name = "USER_NAME")
    @NonNull
    var userName: String,

    @ColumnInfo(name = "PROJECT_ID")
    @NonNull
    var projectID: Long,

    @ColumnInfo(name = "PROJECT_NAME")
    var projectName: String?
)