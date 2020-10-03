package cu.wilb3r.iptvplayerdemo.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [M3U::class],
    version = 1
)
@TypeConverters(Converter::class)
abstract class IPTVDatabase : RoomDatabase(){
    abstract fun getM3UDao(): M3UDao
}