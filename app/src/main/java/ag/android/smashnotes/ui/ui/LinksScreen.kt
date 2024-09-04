package ag.android.smashnotes.ui.ui

import ag.android.smashnotes.Screens
import ag.android.smashnotes.data.Fighter
import ag.android.smashnotes.data.MatchupLink
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

@Composable
fun LinksScreen(
    selectedCharacter: Fighter,
    selectedOpponent: Fighter,
    navigateTo: (String) -> Unit,
    deleteLink: (MatchupLink) -> Unit,
    getMatchupLinks: (String, String) -> Flow<List<MatchupLink>>,
    updateLinksPositions: (List<MatchupLink>) -> Unit
) {
    val charId = selectedCharacter.characterId
    val opponentId = selectedOpponent.characterId

    /**
     *
     *  collectAsState is a Jetpack Compose extension function that collects the Flow and
     *  converts it into a State object. This allows the UI to automatically recompose
     *  whenever the data in the Flow changes.
     */

    val linksState = getMatchupLinks(charId, opponentId).collectAsState(initial = listOf())
    val context = LocalContext.current

    ReorderableListScreen(
        items = linksState.value.sortedBy { it.position },
        buttonText = "Add Link",
        getItemKey = { it.id },
        updatePositions = { list -> updateLinksPositions(list) },
        navigateToNewItem = { navigateTo("${Screens.NewLinkScreen.route}/-1") },
        itemContent = { link, reorderableCollectionItemScope, interactionSource ->

            CardLayout(
                cardContainerColor = MaterialTheme.colorScheme.inversePrimary,
                cardContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                textFontSize = 16.sp,
                cardText = link.linkDesc,
                deleteConfirmationText = "Are you sure you want to permanently delete this link?",
                reorderableCollectionItemScope = reorderableCollectionItemScope,
                interactionSource = interactionSource,
                onCardClick = {
                    val url =
                        if (link.linkText.startsWith("http://") || link.linkText.startsWith("https://")) {
                            link.linkText
                        } else {
                            "http://${link.linkText}"
                        }

                    // Coroutine scope to handle the network request
                    CoroutineScope(Dispatchers.Main).launch {
                        if (isValidUrl(url)) {
                            try {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                }
                                context.startActivity(Intent.createChooser(intent, "Open with"))
                            } catch (e: ActivityNotFoundException) {
                                Toast.makeText(
                                    context,
                                    "No browser found to open the link",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        } else {
                            Toast.makeText(context, "Invalid or Unreachable URL", Toast.LENGTH_LONG)
                                .show()
                        }
                    }


                }, // will navigate to the link
                editCard = { navigateTo("${Screens.NewLinkScreen.route}/${link.id}") },
                deleteCard = { deleteLink(link) }
            )
        },
        copyWithPosition = { item, newPosition -> item.copy(position = newPosition) }
    )
}

suspend fun isValidUrl(urlString: String): Boolean {
    return withContext(Dispatchers.IO) {
        try {
            val client = OkHttpClient.Builder()
                .followRedirects(true)  // Handle redirects
                .followSslRedirects(true)  // Handle SSL redirects
                .build()

            val request = Request.Builder().url(urlString).build()
            val response = client.newCall(request).execute()

            // Check if the response is successful and the final URL is valid
            val isValid = response.isSuccessful
            response.close()

            isValid
        } catch (e: Exception) {
            // Log the exception for debugging purposes
            Log.e("isValidUrl", "Error checking URL: $urlString", e)
            false
        }
    }
}