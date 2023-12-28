package ir.mobfix.aleali.data.models


import com.google.gson.annotations.SerializedName

data class ResponseProf2(
@SerializedName("avatar_url")
val avatarUrl: String?, // /media/upload/21-10-55-5af44ca83-logo.jpg
@SerializedName("birthday")
val birthday: String?, // 2023-12-28
@SerializedName("first_name")
val firstName: String?, // ادمین
@SerializedName("last_name")
val lastName: String?, // ادمینیان
@SerializedName("national_code")
val nationalCode: String?, // 2420431081
@SerializedName("user_groups")
val userGroups: List<UserGroup?>?,
@SerializedName("username")
val username: String? // admin
) {
    data class UserGroup(
        @SerializedName("id")
        val id: Int?, // 3
        @SerializedName("name")
        val name: String? // مدیر
    )
}