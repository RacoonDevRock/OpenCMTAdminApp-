package com.cmt.openctmadminapp.detailReport.ui

import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FileCopy
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.cmt.openctmadminapp.R
import com.cmt.openctmadminapp.core.navigation.Routes
import com.cmt.openctmadminapp.core.ui.shared.buttonNavigate.MyButton

@Composable
fun DetailReportAdminScreen(modifier: Modifier, navigationController: NavHostController) {
    Box(
        modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            HeaderDetailAndTotal(navigationController)

            Spacer(modifier = Modifier.height(20.dp))

            DetailReportContainer(Modifier.weight(1f)) { navigationController.navigate(Routes.TotalReportAdminScreen.route) }

            Spacer(modifier = Modifier.height(20.dp))

            ReportBoxBottom { navigationController.navigate(Routes.HomeAdminScreen.route) }
        }
    }
}

@Composable
fun HeaderDetailAndTotal(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
    ) {
        IconBack(navController, Modifier.align(Alignment.TopStart))
        Image(
            painter = painterResource(id = R.drawable.open_logo_small),
            contentDescription = "Logo CMT",
            Modifier
                .padding(top = 30.dp)
                .align(Alignment.Center),
            contentScale = ContentScale.Fit
        )
    }
}


@Composable
fun IconBack(navController: NavController, modifier: Modifier) {
    Icon(
        imageVector = Icons.Default.ArrowBackIosNew,
        contentDescription = "Retroceso",
        modifier = modifier
            .padding(24.dp)
            .clickable { navController.popBackStack() },
        tint = MaterialTheme.colorScheme.tertiary
    )
}

@Composable
fun ReportBoxBottom(navigate: () -> Unit) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 110.dp, topEnd = 110.dp))
            .background(Color(0xFFD9D9D9))
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .height(170.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = R.string.approve_report_description),
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(25.dp))

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                MyButton(
                    {
                        Toast.makeText(context, "Respuesta enviada", Toast.LENGTH_SHORT).show()
                        navigate()
                    },
                    textButton = stringResource(id = R.string.attend_report_button),
                    myIconButton = Icons.Default.Check
                )
                Spacer(modifier = Modifier.width(5.dp))
                MyButton(
                    {
                        Toast.makeText(context, "Respuesta enviada", Toast.LENGTH_SHORT).show()
                        navigate()
                    },
                    textButton = stringResource(id = R.string.reject_report_button),
                    myIconButton = Icons.Default.Clear
                )
            }
        }
    }
}

@Composable
fun DetailReportContainer(modifier: Modifier, navigate: () -> Unit) {
    Box(
        modifier = modifier
            .padding(horizontal = 25.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer)
    ) {
        val scrollState = rememberScrollState()

        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            ReportHeader("1999", "03/08/2024 10:29")
            Spacer(modifier = Modifier.height(10.dp))
            ReportDetails(
                "Luis Victoria Vega Vilchez",
                "18103629",
                "Los Laureles 126 Urb. California",
                "vvvluisa@gmailcom",
                "985125499",
                "Necesito el informe de la incidencia para presentar en el parte policial de mi denuncia.",
                "1999"
            )
        }
        Icon(
            imageVector = Icons.Default.FileCopy,
            contentDescription = "",
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(8.dp)
                .clickable { navigate() },
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun ReportDetails(
    applicant: String,
    id: String,
    address: String,
    email: String,
    phone: String,
    observers: String,
    nIncident: String,
) {
    MySection(stringResource(id = R.string.applicant_report))
    MySectionData(applicant)
    Spacer(modifier = Modifier.height(3.dp))
    MySection(stringResource(id = R.string.id_report))
    MySectionData(id)
    Spacer(modifier = Modifier.height(3.dp))
    MySection(stringResource(id = R.string.address_report))
    MySectionData(address)
    Spacer(modifier = Modifier.height(3.dp))
    MySection(stringResource(id = R.string.email_report))
    MySectionData(email)
    Spacer(modifier = Modifier.height(3.dp))
    MySection(stringResource(id = R.string.phone_report))
    MySectionData(phone)
    Spacer(modifier = Modifier.height(3.dp))
    MySection(stringResource(id = R.string.observ_report))
    MySectionData(observers)
    Spacer(modifier = Modifier.height(3.dp))
    MySection(stringResource(id = R.string.n_incident_report))
    MySectionData(nIncident)
}

@Composable
fun ReportHeader(incidentNumber: String, date: String) {
    Row(
        Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = "Solicitud N° $incidentNumber",
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

@Composable
fun MySectionData(text: String) {
    Text(
        text = text,
        fontSize = 14.sp,
        fontWeight = FontWeight.ExtraBold,
        textAlign = TextAlign.Justify,
        color = MaterialTheme.colorScheme.primary,
        lineHeight = 17.sp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 5.dp)
    )
}

@Composable
fun MySection(text: String) {
    Text(
        text = text,
        color = MaterialTheme.colorScheme.tertiary,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 12.sp,
        modifier = Modifier.fillMaxWidth()
    )
}

