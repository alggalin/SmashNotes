package ag.android.smashnotes.data

import ag.android.smashnotes.data.categories.CategoryDao
import ag.android.smashnotes.data.links.LinksDao
import ag.android.smashnotes.data.matchups.MatchupDao
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [MatchupNote::class, noteCategory::class, MatchupLink::class], version = 4)
abstract class MatchupDatabase : RoomDatabase() {
    abstract fun CategoryDao(): CategoryDao
    abstract fun MatchupDao(): MatchupDao
    abstract fun LinksDao(): LinksDao
}