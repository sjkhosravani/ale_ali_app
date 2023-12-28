package ir.mobfix.aleali.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.mobfix.aleali.data.models.ResponsePro
import ir.mobfix.aleali.data.models.ResponseProf2
import ir.mobfix.aleali.data.models.profile.BodyUpdateProfile
import ir.mobfix.aleali.data.models.profile.ResponseUpdateProfile
import ir.mobfix.aleali.data.repository.Repository
import ir.mobfix.aleali.utils.network.NetworkRequest
import ir.mobfix.aleali.utils.network.NetworkResponse
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    //new login
    private val _profileData = MutableLiveData<NetworkRequest<ResponseProf2>>()
    val profileData: LiveData<NetworkRequest<ResponseProf2>> = _profileData
    fun profileData(token : String) = viewModelScope.launch {
        _profileData.value = NetworkRequest.Loading()
        val response = repository.profile("Token $token")
        _profileData.value = NetworkResponse(response).generateResponse()
    }

    //update profile
    private val _profileDataUpdate = MutableLiveData<NetworkRequest<ResponseUpdateProfile>>()
    val profileDataUpdate: LiveData<NetworkRequest<ResponseUpdateProfile>> = _profileDataUpdate
    fun profileDataUpdate(token : String,user:String,body : BodyUpdateProfile) = viewModelScope.launch {
        _profileDataUpdate.value = NetworkRequest.Loading()
        val response = repository.updateProfile("Token $token",user,body)
        _profileDataUpdate.value = NetworkResponse(response).generateResponse()
    }


    //Upload Avatar
    private val _avatarData = MutableLiveData<NetworkRequest<Unit>>()
    val avatarData: LiveData<NetworkRequest<Unit>> = _avatarData

    fun callUploadAvatarApi(token:String, user:String,body: RequestBody) = viewModelScope.launch {
        _avatarData.value = NetworkRequest.Loading()
        val response = repository.postUploadAvatar(token,user,body)
        _avatarData.value = NetworkResponse(response).generateResponse()
    }


}
