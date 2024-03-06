package ir.mobfix.aleali.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.mobfix.aleali.data.repository.Repository
import ir.mobfix.aleali.utils.ENGLISH
import ir.mobfix.aleali.utils.FARSI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class SettingsViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    //Spinners
    val languageSelected = MutableLiveData<MutableList<String>>()

    fun loadLang() = viewModelScope.launch(Dispatchers.IO) {
        val data = mutableListOf(FARSI,ENGLISH)
        languageSelected.postValue(data)
    }

}