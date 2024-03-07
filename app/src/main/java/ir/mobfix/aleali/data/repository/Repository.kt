package ir.mobfix.aleali.data.repository

import ir.mobfix.aleali.data.models.login.BodyLogin
import ir.mobfix.aleali.data.models.profile.BodyUpdateProfile
import ir.mobfix.aleali.data.api.ApiServices
import okhttp3.RequestBody
import javax.inject.Inject

class Repository @Inject constructor(private  val api : ApiServices) {

    //new login
    suspend fun userLogin(body: BodyLogin) = api.loginUser(body)

    //Profile
    suspend fun profile(token:String)=api.getProfile(token)

    //update
    suspend fun updateProfile(token : String,user : String,body : BodyUpdateProfile)=api.updateProfile(token,user,body)

    //upload img
    suspend fun postUploadAvatar(token: String,user:String,body: RequestBody) = api.uploadImg(token,user,body)

    suspend fun getMenu(token: String,level : Int) = api.getMenu(token,level)

}
