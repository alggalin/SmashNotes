package ag.android.smashnotes.data

import ag.android.smashnotes.data.matchups.MatchupsRepository
import ag.android.smashnotes.data.categories.CategoryRepository
import ag.android.smashnotes.data.links.LinksRepository
import android.content.Context
import androidx.room.Room

object Graph {
    lateinit var database: MatchupDatabase


    val matchupsRepository by lazy {
        MatchupsRepository(matchupDao = database.MatchupDao())
    }

    val categoryRepository by lazy {
        CategoryRepository(
            categoryDao = database.CategoryDao(),
            matchupsRepository = matchupsRepository
        )
    }

    val linksRepository by lazy {
        LinksRepository(linksDao = database.LinksDao())
    }

    fun provide(context: Context) {
        database = Room.databaseBuilder(context, MatchupDatabase::class.java, "matchups.db")
            .fallbackToDestructiveMigration() // used to clear existing data from database
            .build()
    }

}

