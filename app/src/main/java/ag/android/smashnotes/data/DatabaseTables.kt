package ag.android.smashnotes.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

data class Fighter(
    val name: String,
    val characterId: String,
    val icon: Int
)

@Entity(tableName = "matchups_table", indices = [Index(value = ["categoryId", "note"], unique = true)])
data class MatchupNote(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "characterId")
    val characterId: String,

    @ColumnInfo(name = "opponentId")
    val opponentCharacterId: String,

    @ColumnInfo(name = "categoryId")
    val category: Int,

    @ColumnInfo(name = "note")
    val note: String,

    @ColumnInfo(name = "position")
    val position: Int
)

@Entity(
    tableName = "category_table",
    indices = [Index(value = ["categoryName", "myCharacter", "myOpponent"], unique = true)]
)
data class noteCategory(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "categoryName")
    val name: String,

    @ColumnInfo(name = "myCharacter")
    val myCharacter: String,

    @ColumnInfo(name = "myOpponent")
    val myOpponent: String,

    @ColumnInfo(name = "position")
    val position: Int

)

@Entity(
    tableName = "matchup_links",
    indices = [Index(value = ["description", "linkText"], unique = true)]
)
data class MatchupLink(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "myCharacter")
    val myCharacter: String,

    @ColumnInfo(name = "myOpponent")
    val myOpponent: String,

    @ColumnInfo(name = "description")
    val linkDesc: String,

    @ColumnInfo(name = "linkText")
    val linkText: String,

    @ColumnInfo(name = "position")
    val position: Int


)