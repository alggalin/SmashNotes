package ag.android.smashnotes.ui.ui

import ag.android.smashnotes.data.Fighter
import ag.android.smashnotes.data.MatchupLink
import android.util.Log
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.SharedFlow

@Composable
fun AddNewLinkScreen(
    linkId: Int = -1,
    linkDescriptionState: String,
    linkTextState: String,
    errorMessageOnInsert: SharedFlow<String?>,
    myCharacter: Fighter,
    myOpponent: Fighter,
    navigateBack: () -> Unit,
    addLink: (MatchupLink) -> Unit,
    updateLink: (MatchupLink) -> Unit,
    loadLinkDescriptor: (Int) -> Unit,
    updateLinkDescriptionState: (String) -> Unit,
    updateLinkTextState: (String) -> Unit
) {
    val addOrUpdate = if (linkId == -1) {
        "Add Link"
    } else {
        "Update Link"
    }

    // Load the link information when the screen is first launched
    LaunchedEffect(linkId) {
        loadLinkDescriptor(linkId)
    }

    val displayedErrorMsg = remember {
        mutableStateOf("")
    }

    val errorMessage = errorMessageOnInsert.collectAsState(initial = null).value

    LaunchedEffect(errorMessage) {
        errorMessageOnInsert.collect { errorMessage ->
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
        TextField(
            value = linkDescriptionState,
            onValueChange = { updateLinkDescriptionState(it) },
            label = { Text(text = "Link Description") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        TextField(
            value = linkTextState,
            onValueChange = { updateLinkTextState(it) },
            label = { Text(text = "Link Text") },
            supportingText = {
                Text(
                    text = displayedErrorMsg.value,
                    color = MaterialTheme.colorScheme.error
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
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
                    if (linkId == -1) {
                        // this is a new link
                        addLink(
                            MatchupLink(
                                myCharacter = myCharacter.characterId,
                                myOpponent = myOpponent.characterId,
                                linkDesc = linkDescriptionState.trim(),
                                linkText = linkTextState.trim(),
                                position = 0
                            )
                        )
                    } else { // update existing link
                        Log.d("LinkDesc AddNewLink: ", linkDescriptionState)
                        Log.d("LinkText AddNewLink: ", linkTextState)

                        updateLink(
                            MatchupLink(
                                id = linkId,
                                myCharacter = myCharacter.characterId,
                                myOpponent = myOpponent.characterId,
                                linkDesc = linkDescriptionState.trim(),
                                linkText = linkTextState.trim(),
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