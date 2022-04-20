package ru.netology.nmedia.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import ru.netology.nmedia.dto.Post
import java.io.IOException
import java.util.concurrent.TimeUnit

class PostRepositoryImpl : PostRepository {
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()
    private val gson = Gson()
    private val typeToken = object : TypeToken<List<Post>>() {}

    companion object {
        private const val BASE_URL = "http://10.0.2.2:9999"
        private val jsonType = "application/json".toMediaType()
    }

    override fun getAllAsync(callback: PostRepository.PostCallback<List<Post>>) {
        val request: Request = Request.Builder()
            .url("${BASE_URL}/api/posts")
            .build()

        client.newCall(request)
            .enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    val body = response.body?.string() ?: throw RuntimeException("body is null")
                    try {
                        callback.onSuccess(gson.fromJson(body, typeToken.type))
                    } catch (e: Exception) {
                        callback.onError(e)
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }
            })
    }

    override fun saveAsync(post: Post, callback: PostRepository.PostCallback<Post>) {
        val request: Request = Request.Builder()
            .post(gson.toJson(post).toRequestBody(jsonType))
            .url("${BASE_URL}/api/posts")
            .build()

        client.newCall(request)
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    val body = response.body?.string() ?: throw RuntimeException("body is null")
                    try {
                        callback.onSuccess(gson.fromJson(body, object : TypeToken<Post>() {}.type))
                    } catch (e: Exception) {
                        callback.onError(e)
                    }
                }
            })
    }

    override fun removeByIdAsync(id: Long, callback: PostRepository.PostCallback<Unit>) {
        val request: Request = Request.Builder()
            .delete()
            .url("${BASE_URL}/api/posts/$id")
            .build()

        client.newCall(request)
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    try {
                        callback.onSuccess(Unit)
                    } catch (e: Exception) {
                        callback.onError(e)
                    }
                }
            })
    }

    override fun likeByIdAsync(id: Long, callback: PostRepository.PostCallback<Post>) {
        val request: Request = Request.Builder()
            .post("".toRequestBody())
            .url("${BASE_URL}/api/posts/$id/likes")
            .build()

        client.newCall(request)
            .enqueue(
                object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        callback.onError(e)
                    }

                    override fun onResponse(call: Call, response: Response) {
                        val body = response.body?.string() ?: throw RuntimeException("body is null")
                        try {
                            callback.onSuccess(
                                gson.fromJson(
                                    body,
                                    object : TypeToken<Post>() {}.type
                                )
                            )
                        } catch (e: Exception) {
                            callback.onError(e)
                        }
                    }
                })
    }

    override fun unLikeByIdAsync(id: Long, callback: PostRepository.PostCallback<Post>) {
        val request: Request = Request.Builder()
            .delete()
            .url("${BASE_URL}/api/posts/$id/likes")
            .build()

        client.newCall(request)
            .enqueue(
                object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        callback.onError(e)
                    }

                    override fun onResponse(call: Call, response: Response) {
                        val body = response.body?.string() ?: throw RuntimeException("body is null")
                        try {
                            callback.onSuccess(
                                gson.fromJson(
                                    body,
                                    object : TypeToken<Post>() {}.type
                                )
                            )
                        } catch (e: Exception) {
                            callback.onError(e)
                        }
                    }
                })

    }
}
