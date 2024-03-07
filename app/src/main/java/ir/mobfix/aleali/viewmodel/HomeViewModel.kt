package ir.mobfix.aleali.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.mobfix.aleali.data.models.ResponseMenu
import ir.mobfix.aleali.data.models.ResponseMenu2
import ir.mobfix.aleali.data.repository.Repository
import ir.mobfix.aleali.utils.ERROR_NETWORK
import ir.mobfix.aleali.utils.network.NetworkRequest
import ir.mobfix.aleali.utils.network.NetworkResponse
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    //getMenu
    private val _menuData = MutableLiveData<NetworkRequest<ResponseMenu2>>()
    val menuData: LiveData<NetworkRequest<ResponseMenu2>> = _menuData
    fun getMenu(token: String, level : Int) = viewModelScope.launch {
        _menuData.value = NetworkRequest.Loading()
        try{
            val response = repository.getMenu("Token $token",level)
            _menuData.value = NetworkResponse(response).generateResponse()
        }
        catch (_:Exception){
            _menuData. value = NetworkRequest.Error(ERROR_NETWORK)
        }

    }

}