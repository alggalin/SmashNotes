package ag.android.smashnotes.data.matchups

import ag.android.smashnotes.data.MatchupNote
import android.database.sqlite.SQLiteConstraintException
import kotlinx.coroutines.flow.Flow


// Declares the DAO as a private property in the constructor.
// Passes in the DAO instead of the whole database, because
// only access for DAO is needed

class MatchupsRepository(private val matchupDao: MatchupDao) {

    suspend fun insert(matchupNote: MatchupNote) {
        matchupDao.insert(matchupNote)
    }

    fun getNote(id: Int): Flow<MatchupNote> {
        return matchupDao.getNote(id)
    }

    fun getMatchupNotes(characterId: String, opponentId: String, categoryId: Int): Flow<List<MatchupNote>> {
        return matchupDao.getMatchupNotes(characterId, opponentId, categoryId)
    }

    fun getAllNotes(): Flow<List<MatchupNote>> {
        return matchupDao.getAllNotes()
    }

    suspend fun delete(matchupNote: MatchupNote) {
        matchupDao.delete(matchupNote)
    }

    suspend fun update(matchupNote: MatchupNote) {
        matchupDao.update(matchupNote.id, matchupNote.note)
    }

    suspend fun updateNotes(notes: List<MatchupNote>) {
        matchupDao.updateNotes(notes)
    }

    suspend fun getMaxPosition(): Int {
        return matchupDao.getMaxPosition()
    }

    suspend fun addNote(matchupNote: MatchupNote): Result<Unit> {
        return try {
            // first get the max position to know where to place new entry
            val maxPosition = getMaxPosition()
            val newNote = matchupNote.copy(position = maxPosition + 1)

            // attempt to insert the new entry onto database
            insert(newNote)

            // Return success
            Result.success(Unit)
        } catch (e: SQLiteConstraintException) {
            // Return failure with error message
            Result.failure(Exception("This note already exists!"))
        }
    }

    suspend fun updateNote(matchupNote: MatchupNote): Result<Unit> {
        return try {
            update(matchupNote)

            // Return success
            Result.success(Unit)
        } catch (e: SQLiteConstraintException) {
            // Return failure with error message
            Result.failure(Exception("This note already exists!"))
        }
    }
}