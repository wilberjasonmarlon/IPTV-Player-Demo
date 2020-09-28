package cu.wilb3r.iptvplayerdemo.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "m3items_table")
data class M3U(
    var mDuration: Int,
    @PrimaryKey(autoGenerate = false)
    var mStreamURL: String,
    var mLogoURL: String,
    var mGroupTitle: String,
    var mChannel: String
){
}