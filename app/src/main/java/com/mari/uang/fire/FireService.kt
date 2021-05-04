package com.mari.uang.fire

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.mari.uang.R
import com.mari.uang.config.ConstantConfig
import com.mari.uang.module.splash.SplashActivity
import java.util.*

/**
 *
 * @ProjectName:    Business
 * @Package:        com.mari.uang.fire
 * @ClassName:      FireService
 * @Description:     java类作用描述
 * @Author:         jtao
 * @CreateDate:     2021/5/3 3:00 PM
 * @UpdateUser:     更新者
 * @UpdateDate:     2021/5/3 3:00 PM
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
class FireService : FirebaseMessagingService() {

    private fun sendNotification(
        message: RemoteMessage.Notification,
        data: Map<String, String>?
    ) {

        val appName = getString(R.string.app_name)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val intent = Intent(this, SplashActivity::class.java)
        if (data != null) {
            intent.putExtra(ConstantConfig.PUSH_DATA_GET_KEY, data["url"])
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT
        )
        val defaultSoundUri =
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder: Notification.Builder
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                appName, appName,
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.description = appName
            // Register the channel with system; you can't change the importance
            // or other notification behaviors after this
            notificationManager.createNotificationChannel(channel)
            builder = Notification.Builder(this, appName)
        } else {
            builder = Notification.Builder(this)
        }
        builder.setPriority(Notification.PRIORITY_HIGH)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(message.title)
            .setContentText(message.body)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setDefaults(Notification.DEFAULT_ALL)
            .setContentIntent(pendingIntent)
        notificationManager.notify(
            Random().nextInt() /* ID of notification */,
            builder.build()
        )
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }


    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
    }

}