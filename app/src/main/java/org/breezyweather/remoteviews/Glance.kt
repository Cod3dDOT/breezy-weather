package org.breezyweather.remoteviews

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProviderInfo
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.breezyweather.R
//import org.breezyweather.background.receiver.glance.AppWidgetPinnedReceiver

class Glance {
    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun AppWidgets(modifier: Modifier = Modifier) {
        val context = LocalContext.current
        val widgetManager = AppWidgetManager.getInstance(context)
        // Get a list of our app widget providers to retrieve their info
        val widgetProviders = widgetManager.getInstalledProvidersForPackage(context.packageName, null)

        LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            item {
                AppInfoText()
            }

            // If the launcher does not support pinning request show a banner
            if (!widgetManager.isRequestPinAppWidgetSupported) {
                item {
                    PinUnavailableBanner()
                }
            }

            items(widgetProviders) { providerInfo ->
//                WidgetInfoCard(providerInfo)
            }
        }
    }


    /**
     * Extension method to request the launcher to pin the given AppWidgetProviderInfo
     *
     * Note: the optional success callback to retrieve if the widget was placed might be unreliable
     * depending on the default launcher implementation. Also, it does not callback if user cancels the
     * request.
     */
//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun AppWidgetProviderInfo.pin(context: Context) {
//        val successCallback = PendingIntent.getBroadcast(
//            context,
//            0,
//            Intent(context, AppWidgetPinnedReceiver::class.java),
//            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//        )
//
//        AppWidgetManager.getInstance(context).requestPinAppWidget(provider, null, successCallback)
//    }

    @Composable
    private fun PinUnavailableBanner() {
        Text(
            text = "This launcher does not support pinning request",
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.error),
        )
    }

    @Composable
    private fun AppInfoText() {
        Text(
            text = "App Widgets",
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}