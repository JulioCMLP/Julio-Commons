package com.julioapps.commons.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.julioapps.commons.extensions.baseConfig
import com.julioapps.commons.extensions.getSharedTheme
import com.julioapps.commons.helpers.MyContentProvider

class SharedThemeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == MyContentProvider.SHARED_THEME_ACTIVATED) {
            context.baseConfig.apply {
                if (!wasSharedThemeForced) {
                    wasSharedThemeForced = true
                    isUsingSharedTheme = true
                    wasSharedThemeEverActivated = true

                    context.getSharedTheme {
                        if (it != null) {
                            textColor = it.textColor
                            backgroundColor = it.backgroundColor
                            primaryColor = it.primaryColor
                        }
                    }
                }
            }
        } else if (intent.action == MyContentProvider.SHARED_THEME_UPDATED) {
            context.baseConfig.apply {
                if (isUsingSharedTheme) {
                    context.getSharedTheme {
                        if (it != null) {
                            textColor = it.textColor
                            backgroundColor = it.backgroundColor
                            primaryColor = it.primaryColor
                        }
                    }
                }
            }
        }
    }
}
