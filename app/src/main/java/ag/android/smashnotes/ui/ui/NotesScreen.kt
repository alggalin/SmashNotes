package ag.android.smashnotes.ui.ui

import ag.android.smashnotes.Screens
import ag.android.smashnotes.data.Fighter
import ag.android.smashnotes.data.MatchupNote
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.Flow


@Composable
fun NotesScreen(
    categoryId: Int,
    selectedCharacter: Fighter,
    selectedOpponent: Fighter,
    navigateTo: (String) -> Unit,
    deleteNote: (MatchupNote) -> Unit,
    getMatchupNotes: (String, String, Int) -> Flow<List<MatchupNote>>,
    updateNotesPositions: (List<MatchupNote>) -> Unit
) {
    val charId = selectedCharacter.characterId
    val opponentId = selectedOpponent.characterId

    /**
     *
     *  collectAsState is a Jetpack Compose extension function that collects the Flow and
     *  converts it into a State object. This allows the UI to automatically recompose
     *  whenever the data in the Flow changes.
     */
    val notesState =
        getMatchupNotes(charId, opponentId, categoryId).collectAsState(initial = listOf())

    ReorderableListScreen(
        items = notesState.value.sortedBy { it.position },
        buttonText = "Add Note",
        getItemKey = { it.id },
        updatePositions = { list -> updateNotesPositions(list) },
        navigateToNewItem = { navigateTo("${Screens.NewNoteScreen.route}/-1") },
        itemContent = { note, reorderableCollectionItemScope, interactionSource ->

            CardLayout(
                cardContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
                cardContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                textFontSize = 16.sp,
                cardText = note.note,
                deleteConfirmationText = "Are you sure you want to permanently delete this note?",
                reorderableCollectionItemScope = reorderableCollectionItemScope,
                interactionSource = interactionSource,
                onCardClick = { },
                editCard = { navigateTo("${Screens.NewNoteScreen.route}/${note.id}") },
                deleteCard = { deleteNote(note) })
        },
        copyWithPosition = { item, newPosition -> item.copy(position = newPosition) }
    )
}

