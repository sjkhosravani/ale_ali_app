package ir.mobfix.aleali.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.mobfix.aleali.data.models.login.BodyLogin
import ir.mobfix.aleali.data.models.login.ResponseLogin
import ir.mobfix.aleali.data.repository.Repository
import ir.mobfix.aleali.utils.network.NetworkRequest
import ir.mobfix.aleali.utils.network.NetworkResponse
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    //new login
    private val _loginData = MutableLiveData<NetworkRequest<ResponseLogin>>()
    val loginData: LiveData<NetworkRequest<ResponseLogin>> = _loginData
    fun userLogin(body: BodyLogin) = viewModelScope.launch {
        _loginData.value = NetworkRequest.Loading()
        val response = repository.userLogin(body)
        _loginData.value = NetworkResponse(response).generateResponse()
    }

}