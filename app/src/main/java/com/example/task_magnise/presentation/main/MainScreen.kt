package com.example.task_magnise.presentation.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.task_magnise.data.Data.BARS_COUNT
import com.example.task_magnise.data.Data.CLIENT_ID
import com.example.task_magnise.data.Data.GRANT_TYPE
import com.example.task_magnise.data.Data.INTERVAL
import com.example.task_magnise.data.Data.KIND
import com.example.task_magnise.data.Data.PASSWORD
import com.example.task_magnise.data.Data.PERIODICITY
import com.example.task_magnise.data.Data.PROVIDER
import com.example.task_magnise.data.Data.USERNAME
import com.example.task_magnise.data.model.instruments.Instruments
import com.example.task_magnise.data.model.bars.BarsResponse
import com.example.task_magnise.domain.model.State
import com.example.task_magnise.presentation.chart.ChartView
import com.example.task_magnise.presentation.common.EmptyScreen
import com.example.task_magnise.presentation.common.PeriodicityButton
import com.example.task_magnise.presentation.common.ShimmerEffect
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen(viewModel: MainViewModel = koinViewModel()) {

    val realTimeDataState by viewModel.realTimeDataState.collectAsState()
    val barsState by viewModel.barsState.collectAsState()
    val instrumentsState by viewModel.instrumentsState.collectAsState()
    val selectedPeriodicity by viewModel.selectedPeriodicity.collectAsState()

    var selectedSymbol by remember { mutableStateOf("") }
    var selectedDescription by remember { mutableStateOf("") }
    var selectedSymbolKind by remember { mutableStateOf("") }
    var isDropdownExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.fetchToken(grantType = GRANT_TYPE, clientId = CLIENT_ID, username = USERNAME, password = PASSWORD)
        delay(1000)
        viewModel.fetchInstruments(provider = PROVIDER, kind = KIND)
    }
    
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (realTimeDataState) {
            is State.Loading -> { ShimmerEffect() }
            is State.Error -> { EmptyScreen(realTimeDataState as State.Error) }

            is State.Success -> {
                val message = (realTimeDataState as State.Success).data
                Text("Received data: $message")
            }
        }

        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            when (instrumentsState) {
                is State.Loading -> { ShimmerEffect() }
                is State.Error -> { EmptyScreen(error = instrumentsState as State.Error) }

                is State.Success -> {
                    val instruments = (instrumentsState as State.Success<Instruments>).data.data
                    Text(
                        text = "Selected currency pair: $selectedSymbol",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Button(onClick = { isDropdownExpanded = true }) {
                        Text("Select a currency pair")
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp),
                            verticalArrangement = Arrangement.SpaceBetween,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Kind: $selectedSymbolKind")
                            Text("Description: $selectedDescription")
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    DropdownMenu(
                        expanded = isDropdownExpanded,
                        onDismissRequest = { isDropdownExpanded = false }
                    ) {
                        instruments.forEach { instrument ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        instrument.symbol,
                                        color = Color.White,
                                        fontSize = 15.sp
                                    )
                                },
                                colors = MenuDefaults.itemColors(
                                    textColor = Color.White,
                                    disabledTextColor = Color.White
                                ),
                                onClick = {
                                    selectedSymbol = instrument.symbol
                                    isDropdownExpanded = false
                                    selectedDescription = instrument.description
                                    selectedSymbolKind = instrument.kind
                                    viewModel.selectInstrument(instrument.id)
                                    viewModel.fetchBars(
                                        provider = PROVIDER,
                                        interval = INTERVAL,
                                        periodicity = PERIODICITY,
                                        barsCount = BARS_COUNT
                                    )
                                })
                        }
                    }
                }
            }
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                PeriodicityButton(
                    label = "Hour",
                    selectedPeriodicity = selectedPeriodicity,
                    onClick = { viewModel.setPeriodicity("hour") }
                )
                PeriodicityButton(
                    label = "Minute",
                    selectedPeriodicity = selectedPeriodicity,
                    onClick = { viewModel.setPeriodicity("minute") }
                )
                PeriodicityButton(
                    label = "Day",
                    selectedPeriodicity = selectedPeriodicity,
                    onClick = { viewModel.setPeriodicity("day") }
                )
            }
            Spacer(Modifier.height(16.dp))
            when (barsState) {
                is State.Loading -> { ShimmerEffect() }
                is State.Error -> { EmptyScreen(error = barsState as State.Error) }

                is State.Success -> {
                    val bars = (barsState as State.Success<BarsResponse>).data.data
                    ChartView(bars)
                }
            }
        }
    }
}
