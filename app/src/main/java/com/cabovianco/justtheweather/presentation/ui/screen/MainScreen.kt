package com.cabovianco.justtheweather.presentation.ui.screen

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cabovianco.justtheweather.R
import com.cabovianco.justtheweather.domain.model.Forecast
import com.cabovianco.justtheweather.domain.model.WeatherCondition
import com.cabovianco.justtheweather.domain.model.WeatherEntry
import com.cabovianco.justtheweather.domain.model.WeatherTemperature
import com.cabovianco.justtheweather.presentation.state.MainState
import com.cabovianco.justtheweather.presentation.viewmodel.MainViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun MainScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showInfoDialog by remember { mutableStateOf(false) }

    HandlePermissions(viewModel)

    Scaffold(
        modifier = modifier,
        topBar = {
            TopBar(
                city = uiState.city ?: "...",
                onInfoClick = { showInfoDialog = true }
            )
        },
        bottomBar = {
            BottomBar(
                updatedAt = uiState.updatedAt?.format(DateTimeFormatter.ofPattern("HH:mm"))
                    ?: "...",
                onRefresh = viewModel::onRefresh
            )
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) { padding ->
        val modifier = Modifier
            .fillMaxSize()
            .padding(padding)

        when (val state = uiState.state) {
            is MainState.Success -> MainContent(
                forecast = state.forecast,
                modifier = modifier
            )

            is MainState.Error -> ErrorContent(modifier = modifier)
            is MainState.Loading -> LoadingContent(modifier = modifier)
        }
    }

    if (showInfoDialog) {
        InfoDialog(onDismiss = { showInfoDialog = false })
    }
}

@Composable
private fun HandlePermissions(viewModel: MainViewModel) {
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        ) {
            viewModel.onRefresh()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.askForPermissions.collect {
            launcher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    city: String,
    onInfoClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = "*$city*",
                style = MaterialTheme.typography.titleLarge
            )
        },
        actions = {
            AppButton(
                title = stringResource(R.string.info),
                onClick = onInfoClick
            )
        }
    )
}

@Composable
private fun BottomBar(
    updatedAt: String,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    BottomAppBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppButton(title = stringResource(R.string.refresh), onClick = onRefresh)
            Text(
                text = stringResource(R.string.updated_at, updatedAt),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
private fun AppButton(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextButton(
        modifier = modifier,
        onClick = onClick,
        colors = ButtonDefaults.textButtonColors(
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    ) {
        Text(
            text = "[$title]",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun MainContent(
    forecast: Forecast,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.surface),
        verticalArrangement = Arrangement.spacedBy(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val temperature = forecast.current.temperature as WeatherTemperature.Current

        WeatherCard(
            temperature = temperature.value,
            apparentTemperature = temperature.apparent,
            condition = forecast.current.condition,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        ForecastSection(
            title = stringResource(R.string.next_hours),
            items = forecast.hourly,
            dateTimeFormat = {
                it.format(DateTimeFormatter.ofPattern("HH:mm"))
            }
        )

        ForecastSection(
            title = stringResource(R.string.next_days),
            items = forecast.daily,
            dateTimeFormat = {
                it.format(DateTimeFormatter.ofPattern("EEE"))
            }
        )
    }
}

@Composable
private fun WeatherCard(
    temperature: Int,
    apparentTemperature: Int,
    condition: WeatherCondition,
    modifier: Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "$temperature*",
            style = MaterialTheme.typography.displayLarge
        )

        Text(
            text = condition.format(),
            style = MaterialTheme.typography.bodyLarge
        )

        Text(
            text = stringResource(R.string.feels_like, apparentTemperature),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun ForecastSection(
    title: String,
    items: List<WeatherEntry>,
    dateTimeFormat: (LocalDateTime) -> String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = " $title",
            style = MaterialTheme.typography.bodyLarge.copy(letterSpacing = 0.sp),
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        if (items.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.no_data_available),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                itemsIndexed(items) { index, item ->
                    ForecastCard(
                        item = item,
                        label = dateTimeFormat(item.date).uppercase(),
                        isFirst = index == 0,
                        isLast = index == items.size - 1
                    )
                }
            }
        }
    }
}

@Composable
private fun ForecastCard(
    item: WeatherEntry,
    label: String,
    isFirst: Boolean,
    isLast: Boolean,
    modifier: Modifier = Modifier
) {
    val width = 6
    val top = if (isFirst) "┌" else "┬"
    val bottom = if (isFirst) "└" else "┴"
    val endTop = if (isLast) "┐" else ""
    val endMid = if (isLast) "│" else ""
    val endBot = if (isLast) "┘" else ""

    val textStyle = MaterialTheme.typography.bodyLarge.copy(
        letterSpacing = 0.sp,
        lineHeight = 20.sp
    )

    Column(modifier = modifier) {
        Row {
            Text(text = top, style = textStyle)
            Text(text = "─".repeat(width), style = textStyle)
            if (isLast) Text(text = endTop, style = textStyle)
        }

        Row {
            Text(text = "│", style = textStyle)

            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = " ".repeat(width),
                    style = textStyle.copy(color = androidx.compose.ui.graphics.Color.Transparent)
                )

                Text(text = label, style = textStyle)
            }

            if (isLast) Text(text = endMid, style = textStyle)
        }

        repeat(2) { index ->
            val line = item.condition.toAscii().getOrNull(index) ?: ""

            Row {
                Text(text = "│", style = textStyle)

                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = " ".repeat(width),
                        style = textStyle.copy(color = androidx.compose.ui.graphics.Color.Transparent)
                    )

                    Text(text = line, style = textStyle)
                }

                if (isLast) Text(text = endMid, style = textStyle)
            }
        }

        Row {
            Text(text = "│", style = textStyle)

            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = " ".repeat(width),
                    style = textStyle.copy(color = androidx.compose.ui.graphics.Color.Transparent)
                )

                Text(text = item.temperature.format(), style = textStyle)
            }

            if (isLast) Text(text = endMid, style = textStyle)
        }

        Row {
            Text(text = bottom, style = textStyle)
            Text(text = "─".repeat(width), style = textStyle)
            if (isLast) Text(text = endBot, style = textStyle)
        }
    }
}

private fun WeatherCondition.toAscii(): List<String> = when (this) {
    WeatherCondition.SUN -> listOf("*", "")
    WeatherCondition.CLOUDY -> listOf("(:::)", "")
    WeatherCondition.RAIN -> listOf("/..\\", "''''")
    WeatherCondition.STORM -> listOf("(:::)", "/!\\")
    WeatherCondition.SNOW -> listOf("(:::)", "* * *")
    WeatherCondition.UNKNOWN -> listOf("", "")
}

@Composable
private fun WeatherCondition.format(): String = when (this) {
    WeatherCondition.SUN -> stringResource(R.string.weather_sun)
    WeatherCondition.CLOUDY -> stringResource(R.string.weather_cloudy)
    WeatherCondition.RAIN -> stringResource(R.string.weather_rain)
    WeatherCondition.STORM -> stringResource(R.string.weather_storm)
    WeatherCondition.SNOW -> stringResource(R.string.weather_snow)
    WeatherCondition.UNKNOWN -> stringResource(R.string.weather_unknown)
}

private fun WeatherTemperature.format(): String {
    return when (this) {
        is WeatherTemperature.Current -> ""
        is WeatherTemperature.Hourly -> "$value*"
        is WeatherTemperature.Daily -> "$min/$max*"
    }
}

@Composable
private fun InfoDialog(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            shape = MaterialTheme.shapes.extraSmall
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.ascii_info_title),
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                WeatherCondition.entries.filter { it != WeatherCondition.UNKNOWN }
                    .forEach { condition ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = condition.format(),
                                style = MaterialTheme.typography.bodyLarge
                            )

                            Column(
                                modifier = Modifier.width(80.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                condition
                                    .toAscii()
                                    .filter(String::isNotBlank)
                                    .map(String::trim)
                                    .forEach { line ->
                                        Text(
                                            text = line,
                                            style = MaterialTheme.typography.bodyLarge.copy(
                                                letterSpacing = 0.sp,
                                                lineHeight = 20.sp
                                            )
                                        )
                                    }
                            }
                        }
                    }

                Spacer(modifier = Modifier.height(16.dp))

                AppButton(
                    title = stringResource(R.string.close),
                    onClick = onDismiss
                )
            }
        }
    }
}

@Composable
private fun LoadingContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(R.string.loading),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
private fun ErrorContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(R.string.error),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
