package ag.android.smashnotes.data.categories

import ag.android.smashnotes.data.matchups.MatchupsRepository
import ag.android.smashnotes.data.noteCategory
import android.database.sqlite.SQLiteConstraintException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class CategoryRepository(
    private val categoryDao: CategoryDao,
    private val matchupsRepository: MatchupsRepository
) {

    suspend fun insert(noteCategory: noteCategory) {
        categoryDao.insert(noteCategory)
    }

    suspend fun update(categoryId: Int, newCategoryName: String) {
        categoryDao.updateCategoryName(categoryId, newCategoryName)
    }

    suspend fun delete(noteCategory: noteCategory) {
        categoryDao.delete(noteCategory)
    }

    suspend fun nukeCategories() {
        categoryDao.nukeCategories()
    }

    suspend fun updateCategoryPosition(id: Int, position: Int) {
        categoryDao.updateCategoryPosition(id, position)
    }

    suspend fun updateCategories(categories: List<noteCategory>) {
        categoryDao.updateCategories(categories)
    }

    fun getAllCategories(): Flow<List<noteCategory>> {
        return categoryDao.getAllCategories()
    }

    fun getMatchupCategories(myChar: String, myOpp: String): Flow<List<noteCategory>> {
        return categoryDao.getMatchupCategories(myChar, myOpp)
    }

    fun getCategoryById(categoryId: Int): Flow<noteCategory?> {
        return categoryDao.getCategoryById(categoryId)
    }

    private fun getMaxPosition(): Int {
        return categoryDao.getMaxPosition()
    }

    /**
     * Functions w/ Logic
     */
    suspend fun deleteCategoryWithNotes(noteCategory: noteCategory) {

        // grab the notes that are part of that category and delete them first
        val notesToDelete = matchupsRepository.getMatchupNotes(
            characterId = noteCategory.myCharacter,
            opponentId = noteCategory.myOpponent,
            categoryId = noteCategory.id

        ).firstOrNull() ?: emptyList()

        // delete each note in the list from database
        notesToDelete.forEach { note ->
            matchupsRepository.delete(note)
        }

        // delete the actual category from the database
        delete(noteCategory)

    }

    suspend fun addCategory(noteCategory: noteCategory): Result<Unit> {
        return try {
            // first get the max position to know where to place new entry
            val maxPosition = getMaxPosition()
            val newCategory = noteCategory.copy(position = maxPosition + 1)

            // attempt to insert the new entry onto database
            insert(newCategory)

            // Return success
            Result.success(Unit)
        } catch (e: SQLiteConstraintException) {
            // Return failure with error message
            Result.failure(Exception("This category already exists!"))
        }
    }

    suspend fun updateCategory(categoryId: Int, newCategoryName: String): Result<Unit> {
        return try {
            update(categoryId, newCategoryName)

            // return success
            Result.success(Unit)
        } catch (e: SQLiteConstraintException) {
            // Return failure with error message
            Result.failure(Exception("This category already exists!"))
        }
    }
}

