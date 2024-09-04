package ag.android.smashnotes.ui.ui

import ag.android.smashnotes.data.Fighter
import ag.android.smashnotes.data.MatchupNote
import ag.android.smashnotes.data.noteCategory
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.SharedFlow

// Keep track of the current View State
data class NoteViewState(
    val noteDescription: String,
    val selectedCharacter: Fighter?,
    val selectedOpponent: Fighter?,
    val selectedCategory: noteCategory?,
    val errorMessageOnInsert: SharedFlow<String?>
)

@Composable
fun AddNewNoteScreen(
    viewState: NoteViewState,
    noteId: Int = -1,
    navigateBack: () -> Unit,
    onNoteDescriptionChange: (String) -> Unit,
    addNote: (MatchupNote) -> Unit,
    updateNote: (MatchupNote) -> Unit,
    loadNoteDescription: (Int) -> Unit
) {

    val addOrUpdate = if (noteId == -1) {
        "Add Note"
    } else {
        "Update Note"
    }

    // Load the note description when the screen is first launched
    LaunchedEffect(noteId) {
        loadNoteDescription(noteId)
    }

    val displayedErrorMsg = remember {
        mutableStateOf("")
    }

    val errorMessage = viewState.errorMessageOnInsert.collectAsState(initial = null).value

    LaunchedEffect(errorMessage) {
        viewState.errorMessageOnInsert.collect { errorMessage ->
            if (errorMessage == null) {
                // if there's no error with insertion, navigate up
                navigateBack()
            } else {
                // if there's an error, show the message
                displayedErrorMsg.value = errorMessage

            }
        }
    }


    Column {
        TextField(value = viewState.noteDescription, onValueChange = {
            onNoteDescriptionChange(it)
        }, label = { Text(text = "Note") }, supportingText = {
            Text(
                text = displayedErrorMsg.value, color = MaterialTheme.colorScheme.error
            )
        }, modifier = Modifier
            .fillMaxWidth()
            .padding(all = 8.dp)
        )

        Row(Modifier.weight(1f)) {

            Button(modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp),
                onClick = {
                    navigateBack()
                }) {
                Text(text = "Cancel")
            }

            Button(modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp),
                onClick = {
                    if (noteId == -1) { // adding a new note
                        addNote(
                            MatchupNote(
                                characterId = viewState.selectedCharacter!!.characterId,
                                opponentCharacterId = viewState.selectedOpponent!!.characterId,
                                category = viewState.selectedCategory!!.id,
                                note = viewState.noteDescription.trim(),
                                position = 0
                            )
                        )
                    } else {
                        updateNote(
                            MatchupNote(
                                id = noteId,
                                characterId = viewState.selectedCharacter!!.characterId,
                                opponentCharacterId = viewState.selectedOpponent!!.characterId,
                                category = viewState.selectedCategory!!.id,
                                note = viewState.noteDescription.trim(),
                                position = 0
                            )
                        )
                    }
                }) {
                Text(text = addOrUpdate)
            }

        }
    }

}