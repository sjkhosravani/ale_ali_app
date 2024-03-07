package ir.mobfix.aleali.data.models


import com.google.gson.annotations.SerializedName

class ResponseMenu2 : ArrayList<ResponseMenu2.ResponseMenu2Item>(){
    data class ResponseMenu2Item(
        @SerializedName("children")
        val children: List<Children?>?,
        @SerializedName("color")
        val color: String?, // #31FF83
        @SerializedName("id")
        val id: Int?, // 3
        @SerializedName("image")
        val image: String?, // https://shahid-aleali.site/media/3633dd65-a79b-4da2-9260-b21644a6087f.png
        @SerializedName("level")
        val level: Int?, // 0
        @SerializedName("parent")
        val parent: Any?, // null
        @SerializedName("title")
        val title: String? // حضور و غیاب
    ) {
        data class Children(
            @SerializedName("children")
            val children: List<Any?>?,
            @SerializedName("color")
            val color: String?, // #53FFBA
            @SerializedName("id")
            val id: Int?, // 4
            @SerializedName("image")
            val image: String?, // https://shahid-aleali.site/media/bf36e852-6100-473b-b7f4-68847afd355b.png
            @SerializedName("level")
            val level: Int?, // 1
            @SerializedName("parent")
            val parent: Int?, // 3
            @SerializedName("title")
            val title: String? // ثبت
        )
    }
}