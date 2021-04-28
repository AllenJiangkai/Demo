/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.coupang.common.network

import com.coupang.common.network.impl.MockManager
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

/**
 * @author zli
 * @since 2020/9/24
 */

/**
 * A generic response DTO with Coupang network format.
 */
data class ApiResponse<T>(
    val code: Int? = -1,
    val message: String? = null,
    val data: T? = null,
    val msg : String? = null
) : DTO

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class Result<out R> {

    data class Success<out T>(val data: T) : Result<T>()
    data class GeneralError(val code: Int?, val message: String?,val msg : String?) : Result<Nothing>()
    data class NetworkError(val exception: Exception) : Result<Nothing>()
    object Loading : Result<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is GeneralError -> "GeneralError[code=$code][message=$message]"
            is NetworkError -> "Error[exception=$exception]"
            Loading -> "Loading"
        }
    }
}

/**
 * A helper method to make request in worker thread and handle the generic response errors.
 */
suspend inline fun <reified T : Any> safeApiCall(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    crossinline apiCall: suspend () -> ApiResponse<T>
): Result<T> {
    val requiredType = TypeUtils.type<T>(true)
    return withContext(dispatcher) {
        try {
            val result = apiCall.invoke()
            var code = result.code ?: (if (null == result.data) "" else ApiCode.BIZ_CODE_SUCCESS)
            val data = try {
                TypeUtils.noNullOf(result.data, requiredType, deeply = false)
            } catch (ignore: Exception) {
                Result.GeneralError(result.code, result.message,result.msg)
            }
            assert(null != data) { "create default instance for type $requiredType failure, suggest that require the server to return a no null data" }
            if (ApiCode.BIZ_CODE_SUCCESS == code) {
                Result.Success(data as T)
            } else{
                Result.GeneralError(result.code, result.message,result.msg)
            }
        } catch (cancel: CancellationException) {
            Result.NetworkError(cancel)
        } catch (exception: HttpException) {
            Result.NetworkError(exception)
        } catch (exception: Exception) {
            Result.NetworkError(exception)
        }
    }
}

object ApiCode {
    //success
    const val BIZ_CODE_SUCCESS = 0
}

object NetworkHelper {
    var mockManager: MockManager?=null
    fun init(
        mockManager: MockManager
    ) {
        NetworkHelper.mockManager = mockManager
    }
}

