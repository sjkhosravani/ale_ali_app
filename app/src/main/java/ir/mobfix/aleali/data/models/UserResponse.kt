package ir.mobfix.aleali.data.models


import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("birthday")
    val birthday: Any?, // null
    @SerializedName("first_name")
    val firstName: String?,
    @SerializedName("last_name")
    val lastName: String?,
    @SerializedName("national_code")
    val nationalCode: String?,
    @SerializedName("url")
    val url: String? // https://shahid-aleali.site/api/users/sajad/
)