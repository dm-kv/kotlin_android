package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepositorySQLiteImpl

private val empty = Post()
class PostViewModel(application: Application): AndroidViewModel(application) {

    private val repository: PostRepository = PostRepositorySQLiteImpl(
        AppDb.getInstance(application).postDao)
    val data = repository.get()

    val edited = MutableLiveData(empty)
    fun likeById(id: Long) = repository.likeById(id)
    fun shareById(id: Long) = repository.shareById(id)
    fun removeById(id: Long) = repository.removeById(id)

    fun saveContent(content: String) {
        edited.value?.let { post ->
            val trimmed = content.trim()

            if (post.content != trimmed) {
                repository.save(
                    post.copy(content = trimmed)
                )
            }
            edited.value = empty
        }
    }

    fun edit(post: Post) {
        edited.value = post
    }

}