package ag.android.smashnotes.data.categories

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

const val DATASTORE_NAME = "selected_characters"

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATASTORE_NAME)

class DataStoreRepository(private val context: Context) {
    companion object {
        // DataStore Keys
        val MY_CHARACTER_KEY = stringPreferencesKey("myCharacter")
        val OPPONENT_CHARACTER_KEY = stringPreferencesKey("opponentCharacter")
    }

    // DataStore instance
    private val dataStore = context.dataStore

    suspend fun saveMyCharacter(myCharacterId: String) {
        dataStore.edit { preferences ->
            preferences[MY_CHARACTER_KEY] = myCharacterId
        }
    }

    suspend fun saveOpponentCharacter(opponentId: String) {
        dataStore.edit { preferences ->
            preferences[OPPONENT_CHARACTER_KEY] = opponentId
        }
    }

    suspend fun getSavedCharacter(): String? {
        val preferences = dataStore.data.first()
        return preferences[MY_CHARACTER_KEY]
    }

    suspend fun getSavedOpponent(): String? {
        val preferences = dataStore.data.first()
        return preferences[OPPONENT_CHARACTER_KEY]
    }
}