package com.cmt.openctmadminapp.detailReport.ui

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cmt.openctmadminapp.R
import com.cmt.openctmadminapp.research.ui.HeaderSection

@Composable
fun TotalReportAdminScreen(modifier: Modifier = Modifier) {
    Box(
        modifier
            .fillMaxSize()
            .background(Color(0xFFE5E5E5))
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            HeaderSection()

            Spacer(modifier = Modifier.height(20.dp))


            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.TopCenter) {
                DetailReport()
            }
        }
    }
}

@Composable
fun DetailReport() {
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
            ReportDetailHeader("1999", "03/08/2024 10:29")
            Spacer(modifier = Modifier.height(15.dp))
            ReportDetailsBody(
                "Consumo de licor en la vía pública",
                "Alambre",
                "Cortijo",
                "Persuasiva",
                "Positivo",
                "Contribuyentes refieren que en el lugar se encuentran un grupo de 20 personas ingiriendo bebidas alcohólicas.",
                "Luis Manuel Perez Rengifo",
                "Caminante",
                "Movil 55",
                "T3V-225"
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
    personal: String,
    cargo: String,
    vehicle: String,
    plate: String,
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
            Text(
                text = personal,
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary,
                lineHeight = 17.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 5.dp)
            )
        }
        Column(modifier = Modifier.weight(0.3f)) {
            MySection(stringResource(id = R.string.cargo_filter))
            MySectionData(cargo)
        }
    }
    Spacer(modifier = Modifier.height(3.dp))
    Row(Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.weight(0.7f)) {
            MySection(stringResource(id = R.string.vehicle_filter))
            MySectionData(vehicle)
        }
        Column(modifier = Modifier.weight(0.3f)) {
            MySection(stringResource(id = R.string.plate_filter))
            MySectionData(plate)
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
