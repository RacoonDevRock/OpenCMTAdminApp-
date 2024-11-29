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
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.material3.Typography
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.cmt.openctmadminapp.R
import com.cmt.openctmadminapp.core.navigation.Routes
import com.cmt.openctmadminapp.core.ui.header.FAB
import com.cmt.openctmadminapp.core.ui.header.HeaderSection
import com.cmt.openctmadminapp.core.ui.shared.buttonNavigate.MyButton
import com.cmt.openctmadminapp.core.ui.shared.loading.LoadingScreen
import com.cmt.openctmadminapp.research.data.network.response.SolicitudDTOResponse
import com.cmt.openctmadminapp.research.ui.viewmodel.SearchViewModel

@Composable
fun ResearchAdminScreen(
    modifier: Modifier,
    navigationController: NavHostController,
    onThemeChange: (Int) -> Unit,
    onTypographyChange: (Typography) -> Unit,
    searchViewModel: SearchViewModel = hiltViewModel(),
) {
    var isBottomSheetVisible by rememberSaveable { mutableStateOf(false) }
    val requests = searchViewModel.requestsFlow.collectAsLazyPagingItems()

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

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .constrainAs(list) {
                    top.linkTo(header.bottom)
                    bottom.linkTo(button.top)
                    height = Dimension.fillToConstraints
                }
                .padding(bottom = 10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            when {
                requests.loadState.refresh is LoadState.NotLoading && requests.itemCount == 0 -> {
                    item {
                        NoResultsMessage()
                    }
                }

                else -> {
                    items(requests.itemCount) { index ->
                        val request = requests[index]
                        if (request != null) {
                            ReportBox(
                                {
                                    navigationController.navigate(
                                        Routes.DetailReportAdminScreen.createRoute(
                                            request.nroSolicitud
                                        )
                                    )
                                },
                                request.nroSolicitud,
                                request.fecha,
                                request.hora,
                                request.estado
                            )
                        }
                    }
                }
            }

            when (requests.loadState.append) {
                is LoadState.Loading -> {
                    item {
                        LoadingScreen()
                    }
                }

                is LoadState.Error -> {
                    item {
                        val error = (requests.loadState.append as LoadState.Error).error
                        Text(
                            text = "Error al cargar más elementos: ${error.localizedMessage ?: "Desconocido"}",
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                else -> {}
            }
        }


        FAB(
            isDarkTheme = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES,
            onThemeChange = onThemeChange,
            onMainFabClick = { isBottomSheetVisible = !isBottomSheetVisible },
            currentTypography = MaterialTheme.typography,
            onTypographyChange = onTypographyChange
        )

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


@Composable
fun NoResultsMessage() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(id = R.string.error_message),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.Center)
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
                viewModel.applyFilters(
                    estadoStr = viewModel.estadoStr,
                    periodoStr = viewModel.periodoStr
                )
                onDismiss()
            },
            stringResource(id = R.string.filter_button),
            Icons.Default.Search
        )
    }
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
                            color = MaterialTheme.colorScheme.onTertiary,
                            style = MaterialTheme.typography.bodySmall,
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
                            color = MaterialTheme.colorScheme.onTertiary,
                            style = MaterialTheme.typography.bodySmall
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
        "ACEPTADO" -> MaterialTheme.colorScheme.onSurface
        "RECHAZADO" -> MaterialTheme.colorScheme.surfaceVariant
        "PENDIENTE" -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.primary
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 25.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .clickable { navigate() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "N° $numberIncident",
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = dateIncident,
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = statusIncident,
                    style = MaterialTheme.typography.displaySmall,
                    color = statusColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = hourIncident,
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
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
                style = MaterialTheme.typography.bodySmall,
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