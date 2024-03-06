package ir.mobfix.aleali.data.stored

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore

import dagger.hilt.android.qualifiers.ApplicationContext
import ir.mobfix.aleali.utils.*
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class LanguageLocal @Inject constructor(@ApplicationContext val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(LANGUAGE_LOCAL)
        val isLang = stringPreferencesKey(LANG)

    }

    suspend fun saveLang(lang : String) {
        context.dataStore.edit {
            it[isLang] = lang
        }
    }
    fun getLang() = context.dataStore.data.map {
        it[isLang] ?: FARSI
    }

}