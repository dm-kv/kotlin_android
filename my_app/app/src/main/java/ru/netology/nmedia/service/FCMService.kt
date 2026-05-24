package ru.netology.nmedia.service

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import android.os.Build
import ru.netology.nmedia.R
import android.app.NotificationManager
import android.app.NotificationChannel
import androidx.core.app.NotificationCompat
import android.Manifest
import androidx.core.app.NotificationManagerCompat
import android.app.Notification
import android.content.pm.PackageManager
import kotlin.random.Random
import android.util.Log
import android.content.ContentValues.TAG

class FCMService: FirebaseMessagingService() {

    private val action = "action"
    private val content = "content"
    private val channelId = "remote"
    private val gson = Gson()

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_remote_name)
            val descriptionText = getString(R.string.channel_remote_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    private fun parseAction(actionStr: String?): Action {
        return try {
            Action.valueOf(actionStr ?: "")
        } catch (e: IllegalArgumentException) {
            Action.UNKNOWN
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        val action = parseAction(message.data[content])
        when (action) {
            Action.LIKE -> handleLike(
                gson.fromJson(message.data[content], Like::class.java)

            )
            Action.UNKNOWN -> {
                Log.w(TAG, "Unknown action received: ${message.data[content]}")
            }
        }
    }

    private fun handleLike(content: Like) {
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(
                getString(
                    R.string.notification_user_liked,
                    content.userName,
                    content.postAuthor,
                )
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        notify(notification)

    }

    private fun notify(notification: Notification) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
        ) {
            NotificationManagerCompat.from(this).notify(Random.nextInt(100_000), notification)
        }
    }

    override fun onNewToken(token: String) {
        println(token)
    }

    enum class Action {
        LIKE,
        UNKNOWN

    }

    data class Like(
        val userId: Long,
        val userName: String,
        val postId: Long,
        val postAuthor: String,
    )
}