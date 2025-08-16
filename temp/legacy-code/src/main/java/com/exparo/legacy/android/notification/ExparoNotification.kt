package com.exparo.wallet.android.notification

import android.content.Context
import androidx.core.app.NotificationCompat

class ExparoNotification(
    context: Context,
    val exparoChannel: ExparoNotificationChannel
) : NotificationCompat.Builder(context, exparoChannel.channelId)
