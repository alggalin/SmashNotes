package ag.android.smashnotes
//
//import ag.android.smashnotes.data.matchups.MatchupsRepository
//import ag.android.smashnotes.data.categories.CategoryRepository
//import ag.android.smashnotes.data.Fighter
//import ag.android.smashnotes.data.Graph
//import ag.android.smashnotes.data.MatchupNote
//import ag.android.smashnotes.data.noteCategory
//import android.database.sqlite.SQLiteConstraintException
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.setValue
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.MutableSharedFlow
//import kotlinx.coroutines.flow.SharedFlow
//import kotlinx.coroutines.flow.first
//import kotlinx.coroutines.flow.firstOrNull
//import kotlinx.coroutines.flow.map
//import kotlinx.coroutines.launch
//
//
//class MatchupsViewModel(
//    private val categoryRepository: CategoryRepository = Graph.categoryRepository,
//    private val matchupsRepository: MatchupsRepository = Graph.matchupsRepository
//) : ViewModel() {
//
//    /**
//     * Variables used to keep track of current selections
//     */
////    val selectedCharacter = mutableStateOf<Fighter?>(null)
////    val opponentCharacter = mutableStateOf<Fighter?>(null)
////    val category = mutableStateOf<noteCategory?>(null)
//    var noteDescriptionState by mutableStateOf("")
//
//    private val _errorMessage = MutableSharedFlow<String?>()
//    val errorMessageOnInsert: SharedFlow<String?> = _errorMessage
//
//    fun updateNoteDescriptionState(text: String) {
//        noteDescriptionState = text
//    }
//
//    fun loadNoteDescription(noteId: Int) {
//        if (noteId != -1) {
//            viewModelScope.launch {
//                val currNote = getNote(noteId).first()
//                noteDescriptionState = currNote.note
//            }
//        } else {
//            noteDescriptionState = ""
//        }
//    }
//
////    fun getSelectedCharacter(): Fighter? {
////        return selectedCharacter.value
////    }
////
////    fun getOpponentCharacter(): Fighter? {
////        return opponentCharacter.value
////    }
////
////    fun setCharacter(character: Fighter) {
////        selectedCharacter.value = character
////    }
////
////    fun setOpponent(character: Fighter) {
////        opponentCharacter.value = character
////    }
//
//    lateinit var getAllNotes: Flow<List<MatchupNote>>
//    // lateinit var getAllCategories: Flow<List<noteCategory>>
//
//    init {
//        viewModelScope.launch {
//            // getAllCategories = categoryRepository.getAllCategories()
//            getAllNotes = matchupsRepository.getAllNotes()
//        }
//    }
//
////    /**
////     * Functions used for Category Table
////     */
////    fun setCategory(noteCategory: noteCategory) {
////        category.value = noteCategory
////    }
////
////    fun getCategory(): noteCategory? {
////        return category.value
////    }
////
////    fun getCategoryById(categoryId: Int): Flow<noteCategory> {
////        return categoryRepository.getCategoryById(categoryId).map { category ->
////            category ?: noteCategory(
////                id = -1,
////                name = "",
////                myCharacter = selectedCharacter.value!!.name,
////                myOpponent = opponentCharacter.value!!.name,
////                position = 0
////            )
////        }
////    }
////
////    fun getMatchupCategories(
////        myChar: String, myOpp: String
////    ): Flow<List<noteCategory>> {
////        return categoryRepository.getMatchupCategories(myChar, myOpp)
////    }
////
////    fun addCategory(noteCategory: noteCategory) {
////        viewModelScope.launch(Dispatchers.IO) {
////            try {
////                // first get the max position to know where to place new entry
////                val maxPosition = categoryRepository.getMaxPosition()
////                val newCategory = noteCategory.copy(position = maxPosition + 1)
////
////                categoryRepository.insert(newCategory)
////                _errorMessage.emit(null)
////            } catch (e: SQLiteConstraintException) {
////                _errorMessage.emit("This category already exists!")
////            }
////        }
////    }
////
//    fun updateCategory(categoryId: Int, newCategoryName: String) {
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                categoryRepository.update(categoryId, newCategoryName)
//                _errorMessage.emit(null)
//            } catch (e: SQLiteConstraintException) {
//                _errorMessage.emit("This category already exists!")
//            }
//        }
//    }
////
////    fun updateCategoryPositions(categories: List<noteCategory>) {
////        viewModelScope.launch(Dispatchers.IO) {
////            categoryRepository.updateCategories(categories)
////        }
////    }
////
////    // function deletes a category as well as all the notes contained inside of it
////    fun deleteCategory(noteCategory: noteCategory) {
////        viewModelScope.launch(Dispatchers.IO) {
////            // grab the notes that are part of that category and delete them first
////            val notesToDelete = getMatchupNotes(
////                selectedCharacter.value!!.characterId,
////                opponentCharacter.value!!.characterId,
////                noteCategory.id
////            ).firstOrNull() ?: emptyList()
////
////            // delete each note in the list from database
////            notesToDelete.forEach { note ->
////                deleteNote(note)
////            }
////
////            // delete the actual category from the database
////            categoryRepository.delete(noteCategory)
////        }
////    }
////
////    fun nukeCategories() {
////        viewModelScope.launch(Dispatchers.IO) {
////            categoryRepository.nukeCategories()
////        }
////    }
//
//    /**
//     * Functions used for matchupNote table
//     */
//    fun addNote(matchupNote: MatchupNote) {
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                // first get the max position to know where to place new entry
//                val maxPosition = matchupsRepository.getMaxPosition()
//                val newMatchupNote = matchupNote.copy(position = maxPosition + 1)
//
//                matchupsRepository.insert(newMatchupNote)
//                _errorMessage.emit(null) // clear any previous error message
//            } catch (e: SQLiteConstraintException) {
//                // Handle conflict on insert attempt with user notification
//                _errorMessage.emit("This note already exists!")
//
//            }
//        }
//    }
//
//
//    fun getNote(id: Int): Flow<MatchupNote> {
//        return matchupsRepository.getNote(id)
//    }
//
//    fun getMatchupNotes(
//        characterId: String, opponentId: String, categoryId: Int
//    ): Flow<List<MatchupNote>> {
//        return matchupsRepository.getMatchupNotes(characterId, opponentId, categoryId)
//    }
//
//    fun updateNotesPositions(notes: List<MatchupNote>) {
//        viewModelScope.launch(Dispatchers.IO) {
//            matchupsRepository.updateNotes(notes)
//        }
//    }
//
//    fun deleteNote(matchupNote: MatchupNote) {
//        viewModelScope.launch(Dispatchers.IO) {
//            matchupsRepository.delete(matchupNote)
//        }
//    }
//
//    fun updateNote(matchupNote: MatchupNote) {
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                matchupsRepository.update(matchupNote)
//                _errorMessage.emit(null)
//            } catch (e: SQLiteConstraintException) {
//                _errorMessage.emit("This note already exists!")
//            }
//        }
//    }
//}
//
///**
// * TODO: move this list to another document and delete this one
// */
//
//
