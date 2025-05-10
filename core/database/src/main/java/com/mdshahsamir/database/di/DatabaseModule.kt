package com.mdshahsamir.database.di

import android.content.Context
import androidx.room.Room
import com.mdshahsamir.database.AppDatabase
import com.mdshahsamir.database.migration.MIGRATION_1_2
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    fun bindDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java, "expense-book-database"
        )
            .addMigrations(MIGRATION_1_2)
            .build()
    }
}