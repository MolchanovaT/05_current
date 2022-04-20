package ru.netology.nmedia.repository

import ru.netology.nmedia.dto.Post

interface PostRepository {
    fun getAll(): List<Post>
    fun likeById(id: Long)
    fun save(post: Post)
    fun removeById(id: Long)

    fun getAllAsync(callback: PostCallback<List<Post>>)
    fun saveAsync(post: Post, callback: PostCallback<Post>)

    interface PostCallback<T> {
        fun onSuccess(value: T) {}
        fun onError(e: Exception) {}
    }
}
