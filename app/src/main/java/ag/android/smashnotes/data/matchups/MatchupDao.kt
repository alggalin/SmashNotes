package ag.android.smashnotes.data.matchups

import ag.android.smashnotes.data.MatchupNote
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
abstract class MatchupDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    abstract suspend fun insert(matchupNote: MatchupNote)

    @Query("SELECT * FROM matchups_table WHERE id = :id")
    abstract fun getNote(id: Int): Flow<MatchupNote>

    @Query(
        "SELECT * FROM matchups_table " +
                "WHERE characterId = :characterId " +
            "AND opponentId = :opponentId AND categoryId = :categoryId ORDER BY id ASC")
    abstract fun getMatchupNotes(
        characterId: String, opponentId: String, categoryId: Int
    ): Flow<List<MatchupNote>>

    @Query("SELECT * FROM matchups_table ORDER BY id ASC")
    abstract fun getAllNotes(): Flow<List<MatchupNote>>

    @Delete
    abstract suspend fun delete(matchupNote: MatchupNote)

    @Query("UPDATE matchups_table SET note = :newNote WHERE id = :id")
    abstract suspend fun update(id: Int, newNote: String)

    // Functions relating to positioning
    @Update
    abstract fun updateNotes(matchupNote: List<MatchupNote>)

    @Query("UPDATE matchups_table SET position = :position WHERE id = :id")
    abstract fun updateNotePosition(id: Int, position: Int)

    @Query("SELECT COALESCE(MAX(position), 0) FROM matchups_table")
    abstract fun getMaxPosition(): Int

}