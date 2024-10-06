package com.mdshahsamir.database.di

import com.mdshahsamir.database.AppDatabase
import com.mdshahsamir.database.dao.ExpenseDao
import com.mdshahsamir.database.dao.TransactionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object DaoModule {

    @Provides
    fun provideExpenseDao(appDatabase: AppDatabase): ExpenseDao = appDatabase.expenseDao()

    @Provides
    fun provideTransactionDao(appDatabase: AppDatabase): TransactionDao = appDatabase.transactionDao()
}