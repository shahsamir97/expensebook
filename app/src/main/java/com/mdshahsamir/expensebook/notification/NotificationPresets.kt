package com.mdshahsamir.expensebook.notification

import com.mdshahsamir.expensebook.model.PushNotificationData

enum class ReminderNotificationPresets(val pushNotificationData: PushNotificationData) {
    REMINDER_SAVE_EXPENSE_1(PushNotificationData(
        title = "\uD83D\uDCC5 Did You Track Today’s Spending?",
        body = "Don’t let the day end without recording your expenses. Tap to log now and stay on track!",
        channelId = EbNotificationChannels.REMINDERS.channelId,
    )),

    REMINDER_SAVE_EXPENSE_2(PushNotificationData(
    title = "\uD83D\uDCB0 Don't Forget Your Daily Expenses!",
    body = "Have you saved your expenses for today? Stay on top of your budget and keep your goals in check. Log them now!",
    channelId = EbNotificationChannels.REMINDERS.channelId,
    )),

    REMINDER_SAVE_EXPENSE_3(PushNotificationData(
    title = "\uD83C\uDFAF Keep Your Budget On Point!",
    body = "Tracking your expenses daily makes all the difference. Log today’s spending now!",
    channelId = EbNotificationChannels.REMINDERS.channelId,
    ))
}