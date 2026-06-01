package ru.netology.pusher

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import java.io.FileInputStream


fun main() {
    val options = FirebaseOptions.builder()
        .setCredentials(GoogleCredentials.fromStream(FileInputStream("fcm.json")))
        .build()
    FirebaseApp.initializeApp(options)

    val message = Message.builder()
        .putData("action", "NEW_POST")
        .putData("content", """{
          "userId": 1,
          "userName": "Vasiliy",
          "postId": 2,
          "postAuthor": "Netology",
          "postText": "Привет, это мой новый пост о разработке мобильных приложений! Сегодня поговорим о Kotlin и Android Studio.",
          "notificationTitle": "Vasiliy опубликовал новый пост",
          "notificationBody": "Привет, это мой новый пост о разработке мобильных приложений! Сегодня поговорим о Kotlin и Android Studio."
        }""".trimIndent())
        .setToken(token)
        .build()

    try {
        FirebaseMessaging.getInstance().send(message)
        println("successful send message  ")
    } catch (e: Exception) {
        println("error send message: ${e.message}")
    }
}
