package `in`.dimigo.dimigoin.data.datasource

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.ContextWrapper
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel

class FirebaseCloudMessagingService : FirebaseMessagingService() {
    private val scope = CoroutineScope(Dispatchers.IO)

    override fun onNewToken(newToken: String) {
        Log.d("fcm", "Refreshed token: $newToken")
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        remoteMessage.notification?.let {
            val notiTitle = it.title.toString()
            val notiBody = it.body.toString()

            Log.d("fcm", "$notiTitle / $notiBody")

            LocalNotification(baseContext).sendNotification(
                notiTitle,
                notiBody
            )
        }
    }
}

class LocalNotification(base: Context): ContextWrapper(base)  {
    @SuppressLint("UnspecifiedImmutableFlag")
    fun sendNotification(title: String, body: String) {
        Log.d("func", "sendNotification func start")

        getSystemService(FirebaseMessagingService.NOTIFICATION_SERVICE) as NotificationManager

        val channelId = "Dimigoin"
        val notificationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, channelId)
                .setSmallIcon(applicationContext.applicationInfo.icon)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setTicker(title)
                .setShowWhen(true)
                .setVibrate(longArrayOf(1, 1000))
        val notificationManager = getSystemService(FirebaseMessagingService.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }
        with(NotificationManagerCompat.from(this)) {
            notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
        }
    }

}