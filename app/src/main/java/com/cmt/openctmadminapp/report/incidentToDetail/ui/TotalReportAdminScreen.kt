package com.cmt.openctmadminapp.report.incidentToDetail.ui

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.cmt.openctmadminapp.R
import com.cmt.openctmadminapp.core.ui.header.FAB
import com.cmt.openctmadminapp.core.ui.header.HeaderSection
import com.cmt.openctmadminapp.core.ui.shared.loading.LoadingScreen
import com.cmt.openctmadminapp.report.detail.ui.MySection
import com.cmt.openctmadminapp.report.detail.ui.MySectionData
import com.cmt.openctmadminapp.report.incidentToDetail.data.network.response.IncidenteDTODetail
import com.cmt.openctmadminapp.report.incidentToDetail.data.network.response.PersonalDTO
import com.cmt.openctmadminapp.report.incidentToDetail.data.network.response.VehiculoDTO
import com.cmt.openctmadminapp.report.incidentToDetail.ui.viewmodel.DetailIncidentViewModel

@Composable
fun TotalReportAdminScreen(
    modifier: Modifier = Modifier,
    navigationController: NavHostController,
    nroSolicitud: String,
    onThemeChange: (Int) -> Unit,
    detailIncidentViewModel: DetailIncidentViewModel = hiltViewModel(),
    onTypographyChange: (Typography) -> Unit
) {
    val isLoading by detailIncidentViewModel.isLoading.collectAsState()
    val incidentDetail by detailIncidentViewModel.incidentDetail.collectAsState()

    LaunchedEffect(Unit) {
        detailIncidentViewModel.loadIncidentDetail(nroSolicitud)
    }

    ConstraintLayout(
        modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        val (header, content) = createRefs()

        HeaderSection(
            Modifier.constrainAs(header) { top.linkTo(parent.top) },
            true,
            navigationController
        )

        Box(modifier = Modifier.constrainAs(content) {
            top.linkTo(header.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            bottom.linkTo(parent.bottom)
            height = Dimension.fillToConstraints
        }) {
            if (isLoading) {
                LoadingScreen()
            } else {
                incidentDetail?.let { detail ->
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        DetailReport(modifier, detail)
                    }
                }
            }
        }

        FAB(
            isDarkTheme = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES,
            onThemeChange = onThemeChange,
            { },
            currentTypography = MaterialTheme.typography,
            onTypographyChange = onTypographyChange
        )
    }
}

@Composable
fun DetailReport(modifier: Modifier = Modifier, incidenteDTODetail: IncidenteDTODetail) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 25.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceContainer)
    ) {
        val scrollState = rememberScrollState()

        Column(
            Modifier
                .fillMaxWidth()
                .padding(26.dp)
                .verticalScroll(scrollState)
        ) {
            ReportDetailHeader(incidenteDTODetail.nroIncidente, incidenteDTODetail.fechaHora)
            Spacer(modifier = Modifier.height(10.dp))
            ReportDetailsBody(
                incidenteDTODetail.tipoIncidente,
                incidenteDTODetail.zona,
                incidenteDTODetail.sector,
                incidenteDTODetail.tipoIntervencion,
                incidenteDTODetail.resultadoIntervencion,
                incidenteDTODetail.detalle,
                incidenteDTODetail.personal,
                incidenteDTODetail.vehiculos
            )
        }
    }
}

@Composable
fun ReportDetailsBody(
    incidentType: String,
    zone: String,
    sector: String,
    interventionType: String,
    interventionResult: String,
    detail: String,
    personalList: List<PersonalDTO>,
    vehicleList: List<VehiculoDTO>,
) {
    MySection(stringResource(id = R.string.incident_type_field_filter))
    MySectionData(incidentType)
    Spacer(modifier = Modifier.height(3.dp))

    MySection(stringResource(id = R.string.zone_field_filter))
    MySectionData(zone)
    Spacer(modifier = Modifier.height(3.dp))

    MySection(stringResource(id = R.string.sector_field_filter))
    MySectionData(sector)
    Spacer(modifier = Modifier.height(3.dp))

    MySection(stringResource(id = R.string.intervention_type_filter))
    MySectionData(interventionType)
    Spacer(modifier = Modifier.height(3.dp))

    MySection(stringResource(id = R.string.intervention_result_filter))
    MySectionData(interventionResult)
    Spacer(modifier = Modifier.height(3.dp))

    MySection(stringResource(id = R.string.detail_filter))
    MySectionData(detail)
    Spacer(modifier = Modifier.height(3.dp))


    Row(Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.weight(0.7f)) {
            MySection(stringResource(id = R.string.personal_filter))
            if (personalList.isEmpty()) {
                MySectionData("-")
            } else {
                personalList.forEach { personal ->
                    MySectionData(personal.nombreCompleto)
                }
            }
        }
        Column(modifier = Modifier.weight(0.3f)) {
            MySection(stringResource(id = R.string.cargo_filter))
            if (personalList.isEmpty()) {
                MySectionData("-")
            } else {
                personalList.forEach { personal ->
                    MySectionData(personal.cargo)
                }
            }
        }
    }

    Spacer(modifier = Modifier.height(3.dp))


    Row(Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.weight(0.7f)) {
            MySection(stringResource(id = R.string.vehicle_filter))
            if (personalList.isEmpty()) {
                MySectionData("-")
            } else {
                vehicleList.forEach { vehicle ->
                    MySectionData("Movil ${vehicle.numero}")
                }
            }
        }
        Column(modifier = Modifier.weight(0.3f)) {
            MySection(stringResource(id = R.string.plate_filter))
            if (personalList.isEmpty()) {
                MySectionData("-")
            } else {
                vehicleList.forEach { vehicle ->
                    MySectionData(vehicle.placa)
                }
            }
        }
    }
}

@Composable
fun ReportDetailHeader(incidentNumber: String, date: String) {
    Row(
        Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = "Incidente N° $incidentNumber",
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = date,
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.primary
        )
    }
}
