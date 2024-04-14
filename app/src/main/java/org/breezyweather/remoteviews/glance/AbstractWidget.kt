package org.breezyweather.remoteviews.glance


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
import breezyweather.data.location.LocationRepository
import breezyweather.domain.location.model.Location
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.immutableListOf
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

abstract class AbstractWidget(private val sizes: Set<WidgetSize>) : GlanceAppWidget() {
    companion object {
        private val row = DpSize(180.dp, 48.dp)
        private val row_large = DpSize(300.dp, 48.dp)
        private val column = DpSize(48.dp, 180.dp)
        private val column_large = DpSize(48.dp, 300.dp)
        private val small = DpSize(90.dp, 90.dp)
        private val medium = DpSize(184.dp, 184.dp)
        private val large = DpSize(260.dp, 280.dp)

        enum class WidgetSize {
            ROW,
            ROW_LARGE,
            COLUMN,
            COLUMN_LARGE,
            SMALL,
            MEDIUM,
            LARGE
        }
    }

    private val coroutineContext: CoroutineContext = Job()
    private var locations: ImmutableList<Location> = persistentListOf()

    // Define the supported sizes for this widget.
    // The system will decide which one fits better based on the available space
    override val sizeMode: SizeMode = SizeMode.Responsive(
        sizes.map {
            when (it) {
                WidgetSize.ROW -> row
                WidgetSize.ROW_LARGE -> row_large
                WidgetSize.COLUMN -> column
                WidgetSize.COLUMN_LARGE -> column_large
                WidgetSize.SMALL -> small
                WidgetSize.MEDIUM -> medium
                WidgetSize.LARGE -> large
            }
        }.toSet()
    )

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            val size = LocalSize.current

            //Create the update weather on click lambda
            val context = LocalContext.current
            val scope = rememberCoroutineScope()

            GlanceTheme {
                    when (size) {
                        row -> WeatherRow(context, locations)
                        row_large -> WeatherRowLarge(context, locations)
                        column -> WeatherColumn(context, locations)
                        column_large -> WeatherColumnLarge(context, locations)
                        small -> WeatherSmall(context, locations)
                        medium -> WeatherMedium(context, locations)
                        large -> WeatherLarge(context, locations)
                    }
            } }
    }

    @Composable
    open fun WeatherSmall(context: Context, locations: ImmutableList<Location>) {
        throw NotImplementedError()
    }

    @Composable
    open fun WeatherMedium(context: Context, locations: ImmutableList<Location>) {
        throw NotImplementedError()
    }

    @Composable
    open fun WeatherLarge(context: Context, locations: ImmutableList<Location>) {
        throw NotImplementedError()
    }

    @Composable
    open fun WeatherRow(context: Context, locations: ImmutableList<Location>) {
        throw NotImplementedError()
    }

    @Composable
    open fun WeatherRowLarge(context: Context, locations: ImmutableList<Location>) {
        throw NotImplementedError()
    }

    @Composable
    open fun WeatherColumn(context: Context, locations: ImmutableList<Location>) {
        throw NotImplementedError()
    }

    @Composable
    open fun WeatherColumnLarge(context: Context, locations: ImmutableList<Location>) {
        throw NotImplementedError()
    }

    @Composable
    abstract fun WeatherUnavailable(context: Context)

    fun refreshWeather(context: Context, locations: ImmutableList<Location>) {
        // Force the widget to refresh
        CoroutineScope(coroutineContext).launch {
            this@AbstractWidget.updateAll(context)
            this@AbstractWidget.locations = locations
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun refresh(context: Context) {
        // Force the widget to refresh
        CoroutineScope(coroutineContext).launch {
            this@AbstractWidget.updateAll(context)
        }
    }
}