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

import androidx.compose.runtime.Composable
import androidx.glance.text.Text
import org.breezyweather.common.basic.models.Location
import org.breezyweather.remoteviews.glance.AppWidgetColumn

class ForecastWidget : AbstractWidget(
    setOf(
        Companion.WIDGET_SIZE.THIN,
        Companion.WIDGET_SIZE.SMALL,
        Companion.WIDGET_SIZE.MEDIUM,
        Companion.WIDGET_SIZE.LARGE
    )
) {
    @Composable
    override fun WeatherThin(weatherInfo: Location) {
        AppWidgetColumn {
            Text(text = "hello from thin")
        }
    }

    @Composable
    override fun WeatherSmall(weatherInfo: Location) {
        AppWidgetColumn {
            Text(text = "hello from small")
        }
    }

    @Composable
    override fun WeatherMedium(weatherInfo: Location) {
        AppWidgetColumn {
            Text(text = "hello from medium")
        }
    }

    @Composable
    override fun WeatherLarge(weatherInfo: Location) {
        AppWidgetColumn {
            Text(text = "hello from large")
        }
    }
}