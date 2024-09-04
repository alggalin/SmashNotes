package ag.android.smashnotes.data.links

import ag.android.smashnotes.data.Graph
import ag.android.smashnotes.data.MatchupLink
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

class LinksViewModel(private val linksRepository: LinksRepository = Graph.linksRepository) :
    ViewModel() {
    var linkDescriptionState by mutableStateOf("")
    var linkTextState by mutableStateOf("")

    private val _errorMessage = MutableSharedFlow<String?>()
    val errorMessageOnInsert: SharedFlow<String?> = _errorMessage

    lateinit var getAllLinks: Flow<List<MatchupLink>>

    init {
        viewModelScope.launch {
            getAllLinks = linksRepository.getAllLinks()
        }
    }

    fun updateLinkDescriptionState(linkDesc: String) {
        linkDescriptionState = linkDesc
    }

    fun updateLinkTextState(linkText: String) {
        linkTextState = linkText
    }

    fun loadLinkInfo(linkId: Int) {
        if(linkId != -1) {
            viewModelScope.launch {
                val currLink = linksRepository.getLinkById(linkId).firstOrNull()
                linkDescriptionState = currLink?.linkDesc ?: ""
                linkTextState = currLink?.linkText ?: ""
            }
        } else {
            linkDescriptionState = ""
            linkTextState = ""
        }
    }

    fun addLink(matchupLink: MatchupLink) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = linksRepository.addLink(matchupLink)
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

    fun updateLink(matchupLink: MatchupLink) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = linksRepository.updateLink(matchupLink)
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

    fun getLinkById(id: Int): Flow<MatchupLink> {
        return linksRepository.getLinkById(id)
    }

    fun getMatchupLinks(
        characterId: String, opponentId: String
    ): Flow<List<MatchupLink>> {
        return linksRepository.getMatchupLinks(characterId, opponentId)
    }

    fun updateLinksPositions(links: List<MatchupLink>) {
        viewModelScope.launch(Dispatchers.IO) {
            linksRepository.updateLinksPositions(links)
        }
    }

    fun deleteLink(matchupLink: MatchupLink) {
        viewModelScope.launch(Dispatchers.IO) {
            linksRepository.delete(matchupLink)
        }
    }
}