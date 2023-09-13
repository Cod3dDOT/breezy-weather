/*
 * Copyright 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.breezyweather.remoteviews.glance.widgets

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceTheme
import androidx.glance.LocalContext
import androidx.glance.LocalSize
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.updateAll
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.breezyweather.common.basic.models.Location
import org.breezyweather.db.repositories.LocationEntityRepository
import org.breezyweather.db.repositories.WeatherEntityRepository
import kotlin.coroutines.CoroutineContext

abstract class AbstractWidget(private val sizes: Set<WIDGET_SIZE>) : GlanceAppWidget() {
    companion object {
        private val thinMode = DpSize(120.dp, 120.dp)
        private val smallMode = DpSize(184.dp, 184.dp)
        private val mediumMode = DpSize(260.dp, 200.dp)
        private val largeMode = DpSize(260.dp, 280.dp)

        enum class WIDGET_SIZE {
            THIN,
            SMALL,
            MEDIUM,
            LARGE
        }
    }

    private val coroutineContext: CoroutineContext = Job()
    private var locations: List<Location> = listOf()

    // Define the supported sizes for this widget.
    // The system will decide which one fits better based on the available space
    override val sizeMode: SizeMode = SizeMode.Responsive(
        sizes.map {
            when (it) {
                WIDGET_SIZE.THIN -> thinMode
                WIDGET_SIZE.SMALL -> smallMode
                WIDGET_SIZE.MEDIUM -> mediumMode
                WIDGET_SIZE.LARGE -> largeMode
            }
        }.toSet()
    )

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent { Content() }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun Content() {
        // It will be one of the provided ones
        val size = LocalSize.current

        //Create the update weather on click lambda
        val context = LocalContext.current
        val scope = rememberCoroutineScope()

        GlanceTheme {
            locations.take(1).map {
                when (size) {
                    thinMode -> WeatherThin(it)
                    smallMode -> WeatherSmall(it)
                    mediumMode -> WeatherMedium(it)
                    largeMode -> WeatherLarge(it)
                }
            }
        }
    }

    @Composable
    abstract fun WeatherThin(weatherInfo: Location)

    @Composable
    abstract fun WeatherSmall(weatherInfo: Location)

    @Composable
    abstract fun WeatherMedium(weatherInfo: Location)

    @Composable
    abstract fun WeatherLarge(weatherInfo: Location)

    fun refreshWeather(context: Context) {
        // Force the widget to refresh
        CoroutineScope(coroutineContext).launch {
            this@AbstractWidget.updateAll(context)
            this@AbstractWidget.locations = getWeather()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun refresh(context: Context) {
        // Force the widget to refresh
        CoroutineScope(coroutineContext).launch {
            this@AbstractWidget.updateAll(context)
        }
    }

    private fun getWeather(): List<Location> {
        val locations = LocationEntityRepository.readLocationList().toMutableList()
        return locations.mapIndexed { index, it ->
            it.copy(weather = WeatherEntityRepository.readWeather(locations[index]))
        }
    }
}
