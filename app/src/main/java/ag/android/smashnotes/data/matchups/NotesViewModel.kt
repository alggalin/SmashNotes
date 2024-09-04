package ag.android.smashnotes.data.matchups

import ag.android.smashnotes.data.Graph
import ag.android.smashnotes.data.MatchupNote
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class NotesViewModel(private val matchupsRepository: MatchupsRepository = Graph.matchupsRepository) :
    ViewModel() {

    /**
     * Variables used to keep track of current selections
     */
    var noteDescriptionState by mutableStateOf("")

    private val _errorMessage = MutableSharedFlow<String?>()
    val errorMessageOnInsert: SharedFlow<String?> = _errorMessage

    fun updateNoteDescriptionState(text: String) {
        noteDescriptionState = text
    }

    fun loadNoteDescription(noteId: Int) {
        if (noteId != -1) {
            viewModelScope.launch {
                val currNote = getNote(noteId).firstOrNull()
                noteDescriptionState = currNote?.note ?: ""
            }
        } else {
            noteDescriptionState = ""
        }
    }

    lateinit var getAllNotes: Flow<List<MatchupNote>>

    init {
        viewModelScope.launch {
            getAllNotes = matchupsRepository.getAllNotes()
        }
    }

    /**
     * Functions used for matchupNote table
     */
    fun addNote(matchupNote: MatchupNote) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = matchupsRepository.addNote(matchupNote)
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

    fun updateNote(matchupNote: MatchupNote) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = matchupsRepository.updateNote(matchupNote)
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

    private fun getNote(id: Int): Flow<MatchupNote> {
        return matchupsRepository.getNote(id)
    }

    fun getMatchupNotes(
        characterId: String, opponentId: String, categoryId: Int
    ): Flow<List<MatchupNote>> {
        return matchupsRepository.getMatchupNotes(characterId, opponentId, categoryId)
    }

    fun updateNotesPositions(notes: List<MatchupNote>) {
        viewModelScope.launch(Dispatchers.IO) {
            matchupsRepository.updateNotes(notes)
        }
    }

    fun deleteNote(matchupNote: MatchupNote) {
        viewModelScope.launch(Dispatchers.IO) {
            matchupsRepository.delete(matchupNote)
        }
    }


}