package ir.mobfix.aleali.data.api


import ir.mobfix.aleali.data.models.profile.BodyUpdateProfile
import ir.mobfix.aleali.data.models.ResponseProf2
import ir.mobfix.aleali.data.models.login.BodyLogin
import ir.mobfix.aleali.data.models.login.ResponseLogin
import ir.mobfix.aleali.data.models.profile.ResponseUpdateProfile
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*


interface ApiServices {

    @POST("auth-token/")
    suspend fun loginUser(@Body body: BodyLogin) : Response<ResponseLogin>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @GET("users/me/")
    suspend fun getProfile(@Header("Authorization")token : String): Response<ResponseProf2>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @PATCH("users/{username}/")
    suspend fun updateProfile(@Header("Authorization")token : String,@Path("username") username:String,@Body body : BodyUpdateProfile) : Response<ResponseUpdateProfile>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @PATCH("users/{username}/avatar/")
    suspend fun uploadImg(@Header("Authorization")token : String,@Path("username") username:String,@Body body: RequestBody): Response<Unit>


}