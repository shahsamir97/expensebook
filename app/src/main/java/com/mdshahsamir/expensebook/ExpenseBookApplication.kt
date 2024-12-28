package com.mdshahsamir.expensebook

import android.app.Application
import androidx.work.Configuration
import com.mdshahsamir.expensebook.notification.NotificationWorkerFactory
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class ExpenseBookApplication: Application(), Configuration.Provider {

    @Inject
    lateinit var notificationWorkerFactory: NotificationWorkerFactory

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(notificationWorkerFactory)
            .build()
    }
}