package ir.mobfix.aleali.utils.network


import com.google.gson.Gson
import ir.mobfix.aleali.data.models.ErrorResponse
import ir.mobfix.aleali.utils.ERROR_400
import retrofit2.Response

open class NetworkResponse<T>(private val response: Response<T>) {

    open fun generateResponse(): NetworkRequest<T> {
        return when {
            response.code() == 401 -> NetworkRequest.Error("You are not authorized")
            response.code() == 400 -> NetworkRequest.Error(ERROR_400)
            response.code() == 422 -> {
                var errorMessage = ""
                if (response.errorBody() != null) {
                    val errorResponse = Gson().fromJson(response.errorBody()?.charStream(), ErrorResponse::class.java)
                    //val message = errorResponse.message
                    val errors = errorResponse.non_field_errors
                    errors?.forEach { (_, fieldError) ->
                        errorMessage = fieldError.joinToString()
                    }
                }
                NetworkRequest.Error(errorMessage)
            }

            response.code() == 500 -> NetworkRequest.Error("Try again")
            response.isSuccessful -> NetworkRequest.Success(response.body()!!)
            else -> NetworkRequest.Error(response.message())
        }
    }
}