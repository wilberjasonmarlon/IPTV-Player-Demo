package cu.wilb3r.iptvplayerdemo.data

import cu.wilb3r.iptvplayerdemo.utils.Coroutines
import io.ktor.client.HttpClient
import io.ktor.client.call.call
import io.ktor.client.request.url
import io.ktor.http.HttpMethod
import io.ktor.http.isSuccess
import io.ktor.util.cio.writeChannel
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.io.copyAndClose
import java.io.File

sealed class Result {
    object Success : Result()
    data class Error(val message: String, val cause: Exception? = null) : Result()
    data class Progress(val progress: Int) : Result()
}

suspend fun HttpClient.download(file: File, url: String): Flow<Result> {
    return flow {
        try {
            val response = call {
                url(url)
                method = HttpMethod.Get
            }.response

            if (response.status.isSuccess()) {
                Coroutines.io {
                    response.content.copyAndClose(file.writeChannel())
                }
                emit(Result.Success)
            } else {
                emit(Result.Error("Download error"))
                response.close()
            }
        } catch (e: TimeoutCancellationException) {
            emit(Result.Error("Time out exception", e))
        } catch (t: Throwable) {
            emit(Result.Error("Failed to connect"))
        }
    }
}