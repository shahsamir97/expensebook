package com.mdshahsamir.expensebook.di

import com.mdshahsamir.expensebook.ui.dashboard.DashboardRepository
import com.mdshahsamir.expensebook.ui.dashboard.DashboardRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class DashboardModule {

    @Binds
    abstract fun bindDashboardRepo(dashboardRepositoryImpl: DashboardRepositoryImpl)
            : DashboardRepository
}