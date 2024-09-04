package ag.android.smashnotes.data.links

import ag.android.smashnotes.data.MatchupLink
import android.database.sqlite.SQLiteConstraintException
import kotlinx.coroutines.flow.Flow

class LinksRepository(private val linksDao: LinksDao) {

    suspend fun insert(link: MatchupLink) {
        linksDao.insert(link)
    }

    suspend fun update(matchupLink: MatchupLink) {
        linksDao.updateLink(matchupLink.id, matchupLink.linkDesc, matchupLink.linkText)
    }

    suspend fun delete(link: MatchupLink) {
        linksDao.delete(link)
    }

    fun getLinkById(linkId: Int): Flow<MatchupLink> {
        return linksDao.getLinkById(linkId)
    }

    fun getAllLinks(): Flow<List<MatchupLink>> {
        return linksDao.getAllLinks()
    }

    fun getMatchupLinks(myCharId: String, myOppId: String): Flow<List<MatchupLink>> {
        return linksDao.getMatchupLinks(myCharId, myOppId)
    }

    suspend fun updateLinksPositions(links: List<MatchupLink>) {
        linksDao.updateLinksPositions(links)
    }

    suspend fun getMaxPosition(): Int {
        return linksDao.getMaxPosition()
    }

    suspend fun addLink(matchupLink: MatchupLink): Result<Unit> {
        return try {
            // first get the max position to know where to place new entry
            val maxPosition = getMaxPosition()
            val newLink = matchupLink.copy(position = maxPosition + 1)

            // attempt to insert the new entry into database
            insert(newLink)

            // Return success
            Result.success(Unit)
        } catch (e: SQLiteConstraintException) {
            // Return failure with error message
            Result.failure(Exception("This link already exists!"))
        }
    }

    suspend fun updateLink(matchupLink: MatchupLink): Result<Unit> {
        return try {
            update(matchupLink)

            // Return success
            Result.success(Unit)
        } catch (e: SQLiteConstraintException) {
            // Return failure with error message
            Result.failure(Exception("This link already exists!"))
        }
    }
}