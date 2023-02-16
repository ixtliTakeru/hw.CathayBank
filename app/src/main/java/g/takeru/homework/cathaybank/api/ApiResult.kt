package g.takeru.homework.cathaybank.api

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

sealed class ApiResult<out R> {
    data class Success<D>(val data: D?) : ApiResult<D>()
    data class ApiFailure(val errorMsg: String?) : ApiResult<Nothing>()
    data class ConnectFailure(val throwable: Throwable?) : ApiResult<Nothing>()

    private var successBlock: (suspend (data: R?) -> Unit)? = null
    private var apiFailureBlock: (suspend (errorMsg: String?) -> Unit)? = null
    private var connectFailureBlock: (suspend (throwable: Throwable?) -> Unit)? = null
    private var completeBlock: (suspend () -> Unit)? = null

    fun doOnSuccess(onSuccessBlock: (suspend (data: R?) -> Unit)?): ApiResult<R> {
        this.successBlock = onSuccessBlock
        return this
    }

    fun doOnApiFailure(onFailureBlock: (suspend (errorMsg: String?) -> Unit)?): ApiResult<R> {
        apiFailureBlock = onFailureBlock
        return this
    }

    fun doOnConnectFailure(onFailureBlock: (suspend (throwable: Throwable?) -> Unit)?): ApiResult<R> {
        connectFailureBlock = onFailureBlock
        return this
    }

    fun doOnComplete(onCompleteBlock: (suspend () -> Unit)?): ApiResult<R> {
        completeBlock = onCompleteBlock
        return this
    }

    suspend fun proceed() {
        when (this) {
            is Success -> this.successBlock?.invoke(data)
            is ApiFailure -> this.apiFailureBlock?.invoke(errorMsg)
            is ConnectFailure -> this.connectFailureBlock?.invoke(throwable)
        }
        this.completeBlock?.invoke()
    }
}

suspend fun <T> flowApiResult(
    dispatcher: CoroutineDispatcher,
    block: suspend () -> T
): Flow<ApiResult<T>> =
    flow<ApiResult<T>> {
        emit(ApiResult.Success(block()))
    }.catch {
        emit(it.errorResponse())
    }.flowOn(dispatcher)

suspend fun <T> singleFlowApiResult(
    dispatcher: CoroutineDispatcher,
    block: suspend () -> T
): ApiResult<T> =
    flowApiResult(dispatcher, block).single()

fun Throwable.errorResponse(): ApiResult<Nothing> =
    if (this is HttpException) {
        val errorBody = this.response()?.errorBody()
        if (errorBody != null) {
            ApiResult.ApiFailure(errorBody.string())
        } else {
            ApiResult.ConnectFailure(this)
        }
    } else if (this is UnknownHostException || this is SocketTimeoutException) {
        ApiResult.ConnectFailure(this)
    } else {
        ApiResult.ConnectFailure(this)
    }