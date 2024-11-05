package com.cmt.openctmadminapp.report.incidentToDetail.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.cmt.openctmadminapp.R
import com.cmt.openctmadminapp.core.ui.shared.loading.LoadingScreen
import com.cmt.openctmadminapp.report.detail.ui.HeaderDetailAndTotal
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
    detailIncidentViewModel: DetailIncidentViewModel = hiltViewModel(),
) {
    val isLoading by detailIncidentViewModel.isLoading.collectAsState()
    val incidentDetail by detailIncidentViewModel.incidentDetail.collectAsState()

    LaunchedEffect(Unit) {
        detailIncidentViewModel.loadIncidentDetail(nroSolicitud)
    }

    Box(
        modifier
            .fillMaxSize()
            .background(Color(0xFFE5E5E5))
    ) {

        if (isLoading) {
            LoadingScreen()
        } else {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                HeaderDetailAndTotal(navigationController)

                Spacer(modifier = Modifier.height(20.dp))

                incidentDetail?.let { detail ->
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.TopCenter) {
                        DetailReport(detail)
                    }
                }
            }
        }
    }
}

@Composable
fun DetailReport(incidenteDTODetail: IncidenteDTODetail) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 25.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            ReportDetailHeader(incidenteDTODetail.nroIncidente, incidenteDTODetail.fechaHora)
            Spacer(modifier = Modifier.height(15.dp))
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

    personalList.forEach { personal ->
        Row(Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.weight(0.7f)) {
                MySection(stringResource(id = R.string.personal_filter))
                MySectionData(personal.nombreCompleto)
            }
            Column(modifier = Modifier.weight(0.3f)) {
                MySection(stringResource(id = R.string.cargo_filter))
                MySectionData(personal.cargo)
            }
        }
    }

    Spacer(modifier = Modifier.height(3.dp))

    vehicleList.forEach { vehicle ->
        Row(Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.weight(0.7f)) {
                MySection(stringResource(id = R.string.vehicle_filter))
                MySectionData("Movil ${vehicle.numero}")
            }
            Column(modifier = Modifier.weight(0.3f)) {
                MySection(stringResource(id = R.string.plate_filter))
                MySectionData(vehicle.placa)
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
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = date,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
    }
}
