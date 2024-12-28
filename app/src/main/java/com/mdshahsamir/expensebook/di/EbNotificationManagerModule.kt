package com.mdshahsamir.expensebook.di

import android.app.NotificationManager
import android.content.Context
import com.mdshahsamir.expensebook.notification.EbNotificationManager
import com.mdshahsamir.expensebook.notification.EbNotificationManagerImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class EbNotificationManagerModule {


    @Singleton
    @Binds
    abstract fun bindEbNotificationManager(ebNotificationManagerImpl: EbNotificationManagerImpl)
            : EbNotificationManager
}

@Module
@InstallIn(SingletonComponent::class)
object NotificationManagerModule {

    @Provides
    fun provideNotificationManager(@ApplicationContext context: Context): NotificationManager {
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }
}