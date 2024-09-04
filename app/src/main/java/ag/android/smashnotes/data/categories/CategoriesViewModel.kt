package ag.android.smashnotes.data.categories

import ag.android.smashnotes.data.Fighter
import ag.android.smashnotes.data.Graph
import ag.android.smashnotes.data.noteCategory
import ag.android.smashnotes.ui.ui.getCharacters
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class CategoriesViewModel(
    private val dataStoreRepository: DataStoreRepository,
    private val categoryRepository: CategoryRepository = Graph.categoryRepository
) :
    ViewModel() {

    /**
     * Variables used to keep track of current selections
     */
    val selectedCharacter = mutableStateOf<Fighter?>(null)
    val opponentCharacter = mutableStateOf<Fighter?>(null)
    val category = mutableStateOf<noteCategory?>(null)

    private val _errorMessage = MutableSharedFlow<String?>()
    val errorMessageOnInsert: SharedFlow<String?> = _errorMessage

    fun getSelectedCharacter(): Fighter? {
        return selectedCharacter.value
    }

    fun getOpponentCharacter(): Fighter? {
        return opponentCharacter.value
    }

    fun getSelectedCategory(): noteCategory? {
        return category.value
    }

    private fun getCharacterById(characterId: String): Fighter? {
        return getCharacters().find { character ->
            character.characterId == characterId
        }
    }

    fun setCharacter(character: Fighter) {
        selectedCharacter.value = character

        viewModelScope.launch {
            dataStoreRepository.saveMyCharacter(character.characterId)
        }
    }

    fun setOpponent(character: Fighter) {
        opponentCharacter.value = character

        viewModelScope.launch {
            dataStoreRepository.saveOpponentCharacter(character.characterId)
        }
    }

    lateinit var getAllCategories: Flow<List<noteCategory>>

    // Fetch saved character data from DataStore
    init {
        viewModelScope.launch {
            val myCharacterId = dataStoreRepository.getSavedCharacter()
            val opponentId = dataStoreRepository.getSavedOpponent()

            selectedCharacter.value = myCharacterId?.let { getCharacterById(it) }
            opponentCharacter.value = opponentId?.let { getCharacterById(it) }

            getAllCategories = categoryRepository.getAllCategories()
        }
    }



    /**
     * Functions used for Category Table
     */
    fun setCategory(noteCategory: noteCategory) {
        category.value = noteCategory
    }

    fun getCategory(): noteCategory? {
        return category.value
    }

    fun getCategoryById(categoryId: Int): Flow<noteCategory> {
        return categoryRepository.getCategoryById(categoryId).map { category ->
            category ?: noteCategory(
                id = -1,
                name = "",
                myCharacter = selectedCharacter.value!!.characterId,
                myOpponent = opponentCharacter.value!!.characterId,
                position = 0
            )
        }
    }

    fun getMatchupCategories(
        myChar: String, myOpp: String
    ): Flow<List<noteCategory>> {
        return categoryRepository.getMatchupCategories(myChar, myOpp)
    }

    fun addCategory(noteCategory: noteCategory) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = categoryRepository.addCategory(noteCategory)
            result.fold(
                onSuccess = {
                    _errorMessage.emit(null)
                },
                onFailure = { error ->
                    _errorMessage.emit(error.message)
                }
            )
        }
    }

    fun updateCategory(categoryId: Int, newCategoryName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = categoryRepository.updateCategory(categoryId, newCategoryName)
            result.fold(
                onSuccess = {
                    _errorMessage.emit(null)
                },
                onFailure = { error ->
                    _errorMessage.emit(error.message)
                }
            )
        }
    }


    fun updateCategoryPositions(categories: List<noteCategory>) {
        viewModelScope.launch(Dispatchers.IO) {
            categoryRepository.updateCategories(categories)
        }
    }

    // function deletes a category as well as all the notes contained inside of it
    fun deleteCategory(noteCategory: noteCategory) {
        viewModelScope.launch(Dispatchers.IO) {
            categoryRepository.deleteCategoryWithNotes(noteCategory)
        }
    }

    fun nukeCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            categoryRepository.nukeCategories()
        }
    }
}

// Since CategoriesViewModel has a constructor that requires parameters, factory is needed
class CategoriesViewModelFactory(
    private val dataStoreRepository: DataStoreRepository,
    private val categoryRepository: CategoryRepository
) : ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(CategoriesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CategoriesViewModel(dataStoreRepository, categoryRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}