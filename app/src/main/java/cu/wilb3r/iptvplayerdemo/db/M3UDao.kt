package cu.wilb3r.iptvplayerdemo.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface M3UDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserM3U(item: M3U)

    @Delete
    suspend fun deleteM3U(item: M3U)

    @Query("SELECT * FROM m3items_table")
    fun getAll(): LiveData<List<M3U>>

}