package ag.android.smashnotes.data.links

import ag.android.smashnotes.data.MatchupLink
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
abstract class LinksDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    abstract suspend fun insert(link: MatchupLink)

    @Query("UPDATE matchup_links SET description = :newDescription, linkText = :newLinkText WHERE id = :linkId")
    abstract suspend fun updateLink(linkId: Int, newDescription: String, newLinkText: String)

    @Query("SELECT * FROM matchup_links WHERE id = :linkId")
    abstract fun getLinkById(linkId: Int): Flow<MatchupLink>

    @Query("SELECT * FROM matchup_links")
    abstract fun getAllLinks(): Flow<List<MatchupLink>>

    @Query("SELECT * FROM matchup_links WHERE myCharacter = :myCharacter AND myOpponent = :myOpponent")
    abstract fun getMatchupLinks(
        myCharacter: String, myOpponent: String
    ): Flow<List<MatchupLink>>

    @Update
    abstract fun updateLinksPositions(links: List<MatchupLink>)

    @Query("SELECT COALESCE(MAX(position), 0) FROM matchup_links")
    abstract fun getMaxPosition(): Int

    @Delete
    abstract suspend fun delete(link: MatchupLink)
}