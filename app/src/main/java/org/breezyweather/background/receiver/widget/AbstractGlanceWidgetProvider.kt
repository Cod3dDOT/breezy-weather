package org.breezyweather.background.receiver.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import breezyweather.data.location.LocationRepository
import breezyweather.data.weather.WeatherRepository
import breezyweather.domain.location.model.Location
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.breezyweather.remoteviews.glance.AbstractWidget
import org.breezyweather.remoteviews.presenters.ClockDayDetailsWidgetIMP
import org.breezyweather.remoteviews.presenters.MultiCityWidgetIMP
import javax.inject.Inject

@AndroidEntryPoint
abstract class AbstractGlanceWidgetProvider : GlanceAppWidgetReceiver() {
    @Inject
    lateinit var locationRepository: LocationRepository

    @Inject
    lateinit var weatherRepository: WeatherRepository

    @OptIn(DelicateCoroutinesApi::class)
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        GlobalScope.launch(Dispatchers.IO) {
            val locationList = locationRepository.getXLocations(3, withParameters = false)
            val withWeather = locationList.map {
                it.copy(
                    weather = weatherRepository.getWeatherByLocationId(
                        it.formattedId,
                        withDaily = true,
                        withHourly = true,
                        withMinutely = false,
                        withAlerts = false
                    )
                )
            }

            (glanceAppWidget as AbstractWidget).refreshWeather(
                context,
                withWeather.toImmutableList()
            )
        }
    }
}