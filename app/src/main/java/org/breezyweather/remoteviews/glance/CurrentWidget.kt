package org.breezyweather.remoteviews.glance

import android.content.Context
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.ExperimentalGlanceApi
import androidx.glance.GlanceComposable
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.cornerRadius
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxHeight
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.layout.wrapContentSize
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import breezyweather.domain.location.model.Location
import breezyweather.domain.weather.model.WeatherCode
import kotlinx.collections.immutable.ImmutableList
import org.breezyweather.R
import org.breezyweather.background.receiver.widget.AbstractGlanceWidgetProvider
import org.breezyweather.common.basic.models.options.NotificationTextColor
import org.breezyweather.common.extensions.toBitmap
import org.breezyweather.domain.location.model.isDaylight
import org.breezyweather.remoteviews.Glance
import org.breezyweather.settings.SettingsManager
import org.breezyweather.theme.resource.ResourceHelper
import org.breezyweather.theme.resource.ResourcesProviderFactory

class ResponsiveAppWidgetReceiver : AbstractGlanceWidgetProvider() {
    override val glanceAppWidget: GlanceAppWidget = CurrentWidget()
}

class CurrentWidget : AbstractWidget(
    setOf(
        Companion.WidgetSize.ROW,
        Companion.WidgetSize.ROW_LARGE
    )
) {
    @Composable
    override fun WeatherRow(context: Context, locations: ImmutableList<Location>) {
        val weather = locations.first().weather ?: return WeatherUnavailable(context)
        val code = weather.current?.weatherCode ?: return WeatherUnavailable(context)
        val temp = weather.current?.temperature?.temperature ?: return WeatherUnavailable(context)
        val day = locations.first().isDaylight

        val settings = SettingsManager.getInstance(context)
        val temperatureUnit = settings.temperatureUnit

        Row(
            modifier = GlanceModifier
                .background(GlanceTheme.colors.primaryContainer)
                .cornerRadius(999.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            WeatherIcon(code, day, modifier = GlanceModifier.height(180.dp).width(180.dp).padding(16.dp))
//            Spacer(GlanceModifier.width(50.dp).height(1.dp))
            Text(
                text = temperatureUnit.getShortValueText(context, temp),
                modifier = GlanceModifier,
                style = TextStyle(color = GlanceTheme.colors.secondary, fontSize = 70.sp),
                maxLines = 1
            )
        }
    }

    @Composable
    override fun WeatherRowLarge(context: Context, locations: ImmutableList<Location>) {
        val weather = locations.first().weather ?: return WeatherUnavailable(context)

        return WeatherRow(context, locations)
    }

    @Composable
    override fun WeatherUnavailable(context: Context) {
        Column(
            modifier = GlanceModifier
                .padding(16.dp)
                .cornerRadius(50.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Weather is currently unavailable"
            )
        }
    }

    @Composable
    fun WeatherIcon(weatherCode: WeatherCode, day: Boolean, modifier: GlanceModifier = GlanceModifier) {
        val provider = ResourcesProviderFactory.newInstance

        Image(
            provider = ImageProvider(
                ResourceHelper.getWidgetNotificationIcon(
                    provider,
                    weatherCode,
                    day,
                    false,
                    false
                ).toBitmap()
            ),
            contentDescription = null,
            modifier = modifier
        )
    }
}

