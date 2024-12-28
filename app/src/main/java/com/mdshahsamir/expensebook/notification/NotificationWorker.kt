package com.mdshahsamir.expensebook.notification

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.ListenableWorker
import androidx.work.Worker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import javax.inject.Inject

@HiltWorker
class NotificationWorker @AssistedInject constructor(
    private val ebNotificationManager: EbNotificationManager,
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
) : Worker(context, workerParameters) {

    override fun doWork(): Result {
        Log.d(TAG, "Do Work Called")

        ebNotificationManager.sendNotification(
            context = applicationContext,
            pushNotificationData = ReminderNotificationPresets.entries.random().pushNotificationData
        )

        return Result.success()
    }

    companion object {
        private const val TAG = "NotificationWorker"
        const val KEY_DELAY = "key_delay"
        const val REMINDER_NOTIFICATION_TAG = "reminder_notification_work"
    }
}

class NotificationWorkerFactory @Inject constructor(
    private val ebNotificationManager: EbNotificationManager,
): WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker = NotificationWorker(ebNotificationManager, appContext, workerParameters)
}