package cl.pocketlaw.app.data.remote

import com.google.gson.JsonParseException
import java.io.IOException
import retrofit2.HttpException


suspend fun <T> llamarApi(llamada: suspend () -> T): Result<T> =
    try {
        Result.success(llamada())
    } catch (e: IOException) {
        Result.failure(e)
    } catch (e: HttpException) {
        Result.failure(e)
    } catch (e: JsonParseException) {
        Result.failure(e)
    }
