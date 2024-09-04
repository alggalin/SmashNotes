package ag.android.smashnotes.data.categories

import ag.android.smashnotes.data.noteCategory
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
abstract class CategoryDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    abstract suspend fun insert(category: noteCategory)

    @Query("UPDATE category_table SET categoryName = :newCategoryName WHERE id = :categoryId")
    abstract suspend fun updateCategoryName(categoryId: Int, newCategoryName: String)

    @Delete
    abstract suspend fun delete(noteCategory: noteCategory)

    @Query("SELECT * FROM category_table")
    abstract fun getAllCategories(): Flow<List<noteCategory>>

    @Query("SELECT * FROM category_table WHERE myCharacter = :myChar AND myOpponent = :myOpp")
    abstract fun getMatchupCategories(
        myChar: String, myOpp: String
    ): Flow<List<noteCategory>>

    @Query("SELECT * FROM category_table WHERE id = :categoryId")
    abstract fun getCategoryById(categoryId: Int): Flow<noteCategory?>

    @Query("DELETE FROM category_table")
    abstract fun nukeCategories()

    // Update a list of categories with new positions
    @Update
    abstract suspend fun updateCategories(categories: List<noteCategory>)

    // Update a single category's position
    @Query("UPDATE category_table SET position = :position WHERE id = :id")
    abstract fun updateCategoryPosition(id: Int, position: Int)

    @Query("SELECT COALESCE(MAX(position),0) FROM category_table")
    abstract fun getMaxPosition(): Int
}