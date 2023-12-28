package ir.mobfix.aleali.data.stored

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore

import dagger.hilt.android.qualifiers.ApplicationContext
import ir.mobfix.aleali.utils.PERFORMED
import ir.mobfix.aleali.utils.STORE_PERFORMED
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class StorePerformed @Inject constructor(@ApplicationContext val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(STORE_PERFORMED)
        val isPerformed = booleanPreferencesKey(PERFORMED)

    }

    suspend fun savePerformed(status : Boolean) {
        context.dataStore.edit {
            it[isPerformed] = status
        }
    }
    fun getPerformed() = context.dataStore.data.map {
        it[isPerformed] ?: false
    }

}