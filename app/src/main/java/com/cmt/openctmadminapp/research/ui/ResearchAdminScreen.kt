package com.cmt.openctmadminapp.research.ui

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.cmt.openctmadminapp.R
import com.cmt.openctmadminapp.core.navigation.Routes
import com.cmt.openctmadminapp.core.ui.header.FAB
import com.cmt.openctmadminapp.core.ui.header.HeaderSection
import com.cmt.openctmadminapp.core.ui.shared.buttonNavigate.MyButton
import com.cmt.openctmadminapp.core.ui.shared.loading.LoadingScreen
import com.cmt.openctmadminapp.research.ui.viewmodel.SearchViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun ResearchAdminScreen(
    modifier: Modifier,
    navigationController: NavHostController,
    onThemeChange: (Int) -> Unit,
    searchViewModel: SearchViewModel = hiltViewModel(),
) {
    val uiState by searchViewModel.uiState.collectAsState()
    var isBottomSheetVisible by rememberSaveable { mutableStateOf(false) }
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = uiState.isLoading)
    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        searchViewModel.loadAllSolicitudes()
    }

    ConstraintLayout(modifier = modifier.fillMaxSize()) {
        val (header, list, button) = createRefs()

        HeaderSection(
            Modifier.constrainAs(header) { top.linkTo(parent.top) },
            false,
            navigationController
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(list) {
                    top.linkTo(header.bottom)
                    bottom.linkTo(button.top)
                    height = Dimension.fillToConstraints
                },
            verticalArrangement = Arrangement.Center
        ) {

            SwipeRefresh(
                state = swipeRefreshState,
                onRefresh = { searchViewModel.loadAllSolicitudes() },
                modifier = Modifier.weight(1f)
            ) {
                when {
                    uiState.isLoading && !swipeRefreshState.isRefreshing -> {
                        LoadingScreen()
                    }

                    uiState.errorMessage != null -> {
                        uiState.errorMessage?.let { errorMessage ->
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = errorMessage,
                                    color = Color.Red,
                                    modifier = Modifier.align(Alignment.Center),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    else -> {
                        LazyColumn(
                            state = listState,
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            items(uiState.solicitudes) { solicitud ->
                                ReportBox(
                                    {
                                        navigationController.navigate(
                                            Routes.DetailReportAdminScreen.createRoute(
                                                solicitud.nroSolicitud
                                            )
                                        )
                                    },
                                    solicitud.nroSolicitud,
                                    solicitud.fecha,
                                    solicitud.hora,
                                    solicitud.estado
                                )
                            }
                        }

                        LaunchedEffect(listState) {
                            snapshotFlow {
                                listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
                            }.collect { lastVisibleItem ->
                                lastVisibleItem?.let {
                                    if (it == uiState.solicitudes.size - 1 && !uiState.isLoading) {
                                        searchViewModel.loadNextPage()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(80.dp))

        FAB(
            isDarkTheme = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES,
            onThemeChange = onThemeChange, onMainFabClick = { isBottomSheetVisible = !isBottomSheetVisible })

        if (isBottomSheetVisible) {
            BottomSheetWithContent(
                searchViewModel,
                onDismiss = { isBottomSheetVisible = false })
        }

        MyButton(
            navigate = { isBottomSheetVisible = true },
            textButton = stringResource(id = R.string.message_filter),
            myIconButton = Icons.Default.KeyboardArrowUp,
            modifier = Modifier
                .padding(bottom = 10.dp)
                .constrainAs(button) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetWithContent(searchViewModel: SearchViewModel, onDismiss: () -> Unit) {
    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        modifier = Modifier
            .fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        shape = RoundedCornerShape(topStart = 110.dp, topEnd = 110.dp)
    ) {
        BottomSheetContent(searchViewModel, onDismiss)
    }
}

@Composable
fun BottomSheetContent(viewModel: SearchViewModel, onDismiss: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.title_filter),
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(10.dp))

        EstadoDropdown(
            selectedEstado = viewModel.estadoStr,
            onEstadoSelected = { viewModel.estadoStr = it }
        )

        PeriodoDropdown(
            selectedPeriodo = viewModel.periodoStr,
            onPeriodoSelected = { viewModel.periodoStr = it }
        )

        MyButton(
            {
                viewModel.loadAllSolicitudes()
                onDismiss()
            },
            stringResource(id = R.string.filter_button),
            Icons.Default.Search
        )
    }
    Spacer(modifier = Modifier.width(56.dp))
}

@Composable
fun PeriodoDropdown(selectedPeriodo: String?, onPeriodoSelected: (String?) -> Unit) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    val periodos = mapOf(
        "Última hora" to "LAST_HOUR",
        "Últimas 24 horas" to "LAST_24_HOURS",
        "Última semana" to "LAST_WEEK",
        "Último mes" to "LAST_MONTH",
        "Último año" to "LAST_YEAR",
        "Todo el tiempo" to null
    )

    Box {

        MyTextField(
            periodos.entries.find { it.value == selectedPeriodo }?.key ?: "Tiempo",
            {},
            stringResource(id = R.string.time_field_filter),
            trailingIcon = {
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    modifier = Modifier.clickable { expanded = true }
                )
            },
            Modifier.fillMaxWidth()
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.primary)
                .align(Alignment.Center),
        ) {
            periodos.forEach { (displayText, value) ->
                DropdownMenuItem(onClick = {
                    onPeriodoSelected(value)
                    expanded = false
                },
                    text = {
                        Text(
                            text = displayText,
                            color = MaterialTheme.colorScheme.onTertiary
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun EstadoDropdown(selectedEstado: String?, onEstadoSelected: (String?) -> Unit) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    val estados = mapOf(
        "Todos los estados" to null,
        "Pendiente" to "PENDIENTE",
        "Aceptado" to "ACEPTADO",
        "Rechazado" to "RECHAZADO"
    )

    Box {
        MyTextField(
            estados.entries.find { it.value == selectedEstado }?.key ?: "Estado",
            {},
            stringResource(id = R.string.state_field_filter),
            trailingIcon = {
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    modifier = Modifier.clickable { expanded = true }
                )
            },
            Modifier
                .fillMaxWidth()
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.primary)
                .align(Alignment.Center)
        ) {
            estados.forEach { (displayText, value) ->
                DropdownMenuItem(
                    onClick = {
                        onEstadoSelected(value)
                        expanded = false
                    },
                    text = {
                        Text(
                            text = displayText,
                            color = MaterialTheme.colorScheme.onTertiary
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun ReportBox(
    navigate: () -> Unit,
    numberIncident: String,
    dateIncident: String,
    hourIncident: String,
    statusIncident: String,
) {

    val statusColor = when (statusIncident) {
        "ACEPTADO" -> Color(0xFF32A91D)
        "RECHAZADO" -> Color(0xFFFF9F19)
        "PENDIENTE" -> Color(0xFFD71414)
        else -> MaterialTheme.colorScheme.primary
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(85.dp)
            .padding(start = 26.dp, end = 26.dp, bottom = 16.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .clickable { navigate() }
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Solicitud N° $numberIncident",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.primary,
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = dateIncident,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.primary,
                    lineHeight = 20.sp
                )
                Text(
                    text = hourIncident,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.primary,
                    lineHeight = 20.sp,
                    modifier = Modifier.padding(start = 3.dp)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = statusIncident,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 14.sp,
                color = statusColor,
                lineHeight = 20.sp,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
fun MyTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    trailingIcon: @Composable () -> Unit,
    modifier: Modifier,
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholder,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(start = 8.dp),
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onTertiary
            )
        },
        readOnly = true,
        modifier = modifier
            .padding(start = 30.dp, end = 30.dp, bottom = 13.dp)
            .height(50.dp),
        trailingIcon = trailingIcon,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            unfocusedTextColor = MaterialTheme.colorScheme.onTertiary,
            unfocusedTrailingIconColor = MaterialTheme.colorScheme.onTertiary,
            focusedTextColor = MaterialTheme.colorScheme.onTertiaryContainer,
            focusedTrailingIconColor = MaterialTheme.colorScheme.onTertiaryContainer,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = MaterialTheme.colorScheme.onTertiaryContainer,
        ),
        shape = RoundedCornerShape(25.dp)
    )
}