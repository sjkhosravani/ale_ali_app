package ir.mobfix.aleali.data.models

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("errors")
    val non_field_errors: Map<String, List<String>>?,

)