package com.mdshahsamir.expensebook.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.VISIBILITY_PUBLIC
import androidx.core.app.TaskStackBuilder
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.mdshahsamir.expensebook.MainActivity
import com.mdshahsamir.expensebook.R
import com.mdshahsamir.expensebook.model.PushNotificationData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

interface EbNotificationManager {
    fun sendNotification(context: Context, pushNotificationData: PushNotificationData)
    suspend fun scheduleNotifications()
}

class EbNotificationManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val notificationManager: NotificationManager,
): EbNotificationManager {

    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    init {
        if(notificationManager.notificationChannels.isEmpty()) {
            createNotificationChannels()
        }
    }

    private fun createNotificationChannels() {
        val channel = NotificationChannel(
            EbNotificationChannels.REMINDERS.channelId,
            EbNotificationChannels.REMINDERS.channelName,
            NotificationManager.IMPORTANCE_HIGH
        )

        notificationManager.createNotificationChannel(channel)
    }

    override fun sendNotification(context: Context, pushNotificationData: PushNotificationData) {
        coroutineScope.launch {
            val pendingIntent = createNotificationIntent(context)
            val notificationBuilder = buildNotification(context, pushNotificationData, pendingIntent)

            notificationManager.notify(0, notificationBuilder.build())
        }
    }

    private fun buildNotification(
        context: Context,
        pushNotificationData: PushNotificationData,
        pendingIntent: PendingIntent
    ): NotificationCompat.Builder {
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        return NotificationCompat.Builder(context, pushNotificationData.channelId)
            .setContentTitle(pushNotificationData.title)
            .setContentText(pushNotificationData.body)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setVisibility(VISIBILITY_PUBLIC)
            .setContentIntent(pendingIntent)
    }

    private fun createNotificationIntent(
        context: Context,
    ): PendingIntent {
        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)

        val pendingIntent =
            TaskStackBuilder.create(context).run {
                addNextIntentWithParentStack(intent)
                getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE)
            }

        return pendingIntent ?: PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
    }

    override suspend fun scheduleNotifications() {
        val workManager = WorkManager.getInstance(context)

        coroutineScope.launch(Dispatchers.IO) {
            if (workManager.getWorkInfosByTag(UNIQUE_WORK_NAME).get()
                    .isEmpty() || workManager.getWorkInfosByTag(UNIQUE_WORK_NAME).isCancelled)
            {
                scheduleExpenseTrackingReminderNotifications(workManager)
            }
        }
    }

    private fun scheduleExpenseTrackingReminderNotifications(workManager: WorkManager) {
        val periodicWork =
            PeriodicWorkRequestBuilder<NotificationWorker>(PERIODIC_WORK_INTERVAL, TimeUnit.HOURS)
                .setInitialDelay(PERIODIC_WORK_INTERVAL, TimeUnit.HOURS)
                .addTag(UNIQUE_WORK_NAME)
                .build()

        workManager.enqueueUniquePeriodicWork(
            UNIQUE_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            periodicWork
        )
    }

    companion object {
        const val TAG = "ScrapbaysNotificationManager"
        const val UNIQUE_WORK_NAME = "Reminder Notification Work"
        const val PERIODIC_WORK_INTERVAL = 24L
    }
}
