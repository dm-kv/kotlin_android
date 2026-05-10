package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.entity.PostEntity

class PostRepositorySQLiteImpl(
    private val dao: PostDao
) : PostRepository {

    override fun get(): LiveData<List<Post>> = dao.get().map { posts ->
        posts.map {entity ->
            entity.toPost()
        }
    }

    override fun save(post: Post) {
        dao.save(PostEntity.fromPost(post))
    }

    override fun likeById(id: Long) {
        dao.likeById(id)
    }

    override fun shareById(id: Long) {
        dao.shareById(id)
    }

    override fun removeById(id: Long) {
        dao.removeById(id)
    }
}

