package ir.mobfix.aleali.data.models.profile


import com.google.gson.annotations.SerializedName

data class ResponseUpdateProfile(
    @SerializedName("avatar")
    val avatar: String?, // string
    @SerializedName("birthday")
    val birthday: String?, // 2023-12-28
    @SerializedName("first_name")
    val firstName: String?, // string
    @SerializedName("last_name")
    val lastName: String?, // string
    @SerializedName("national_code")
    val nationalCode: String? // string
)