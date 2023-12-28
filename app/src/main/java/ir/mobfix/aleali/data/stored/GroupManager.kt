package ir.mobfix.aleali.data.stored


import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import ir.mobfix.aleali.utils.G_AUTH_DATA
import ir.mobfix.aleali.utils.G_DATA
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GroupManager @Inject constructor(@ApplicationContext private val context: Context) {
    private val appContext = context.applicationContext

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(G_AUTH_DATA)
        private val USER_TYPE = stringPreferencesKey(G_DATA)
    }

    suspend fun saveType(token: String) {
        appContext.dataStore.edit {
            it[USER_TYPE] = token
        }
    }

    val getType: Flow<String?> = appContext.dataStore.data.map {
        it[USER_TYPE]
    }

    suspend fun clearType() {
        appContext.dataStore.edit {
            it.clear()
        }
    }
}