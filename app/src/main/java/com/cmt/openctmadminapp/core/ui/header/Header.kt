package com.cmt.openctmadminapp.core.ui.header

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.cmt.openctmadminapp.R


@Composable
fun HeaderSection(modifier: Modifier = Modifier, onBack: Boolean = false, navController: NavController) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(140.dp)
    ) {
        if (onBack) {
            IconBack(navController, Modifier.align(Alignment.TopStart))
        }

        Image(
            painter = painterResource(id = R.drawable.open_logo_small),
            contentDescription = "Logo CMT",
            Modifier
                .align(Alignment.Center)
                .height(80.dp),
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
